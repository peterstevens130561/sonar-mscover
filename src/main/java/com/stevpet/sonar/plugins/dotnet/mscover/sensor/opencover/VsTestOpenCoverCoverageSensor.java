package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.command.CommandExecutor;
import org.sonar.plugins.dotnet.api.DotNetConstants;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;
import org.sonar.plugins.dotnet.api.sensor.AbstractDotNetSensor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ConcreteParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.opencover.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.opencover.OpenCoverTarget;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.UnitTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.UnitTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
@DependsUpon(DotNetConstants.CORE_PLUGIN_EXECUTED)
public class VsTestOpenCoverCoverageSensor extends AbstractDotNetSensor {

    private static String WONT_EXECUTE = "VsTest.Console using OpenCover.Console.Exe won't execute as ";
    private static final Logger LOG = LoggerFactory.getLogger(VsTestOpenCoverCoverageSensor.class);
    private VisualStudioSolution solution;
    private File workDir;
    private PropertiesHelper propertiesHelper ;
    private final Settings settings;
    private ModuleFileSystem moduleFileSystem;
    private String openCoverCoveragePath;
    private VsTestEnvironment testEnvironment;
    private String sonarWorkingDirPath;
    
    public VsTestOpenCoverCoverageSensor(Settings settings, 
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            ModuleFileSystem moduleFileSystem,
            VsTestEnvironment testEnvironment) {
        super(microsoftWindowsEnvironment, "OpenCover", PropertiesHelper.MSCOVER_MODE);
        propertiesHelper = PropertiesHelper.create(settings);
        this.settings = settings;
        this.moduleFileSystem = moduleFileSystem;
        this.testEnvironment = testEnvironment;
    }

    @Override
    public String[] getSupportedLanguages() {
        return new String[] {"cs"};
    }


    /**
     * {@inheritDoc}
     */
    public boolean shouldExecuteOnProject(Project project) {
        if (getMicrosoftWindowsEnvironment().isTestExecutionDone()) {
            logReasonToNotExecute("test execution has already been done.");
            return false;
        }
        if (getMicrosoftWindowsEnvironment().getCurrentSolution() != null
                && getMicrosoftWindowsEnvironment().getCurrentSolution()
                        .getUnitTestProjects().isEmpty()) {
            logReasonToNotExecute("there are no test projects.");
            return false;
        }
        if (project.isRoot() || !"cs".equals(project.getLanguageKey())) {
            return false;
        }
        if (propertiesHelper.runOpenCover()) {
            LOG.info("will run opencover with vstest embedded");
            return true;
        }
        return false;
    }
    private void logReasonToNotExecute(String string) {
        LOG.info(WONT_EXECUTE + string);
    }

    @Override
    public void analyse(Project project, SensorContext context) {
        sonarWorkingDirPath = project.getFileSystem().getSonarWorkingDirectory().getAbsolutePath();
        String openCoverCoveragePath= sonarWorkingDirPath + "\\coverage-report.xml";
        testEnvironment.setCoverageXmlPath(openCoverCoveragePath);
        getSolution();
        ensureWorkDirExists();
        
        executeVsTestOpenCoverRunner(project);
        // tell that tests were executed so that no other project tries to launch them a second time
        getMicrosoftWindowsEnvironment().setTestExecutionDone();
        parseCoverageFile(testEnvironment);
        
     
    }

    private void parseCoverageFile(VsTestEnvironment testEnvironment) {
        ParserFactory parserFactory = new ConcreteParserFactory();
        SonarCoverage sonarCoverageRegistry = new SonarCoverage();
        ParserSubject parser=parserFactory.createOpenCoverParser(sonarCoverageRegistry);
        parser.parseFile(new File(openCoverCoveragePath));
        testEnvironment.setSonarCoverage(sonarCoverageRegistry);
    }

    
    private void executeVsTestOpenCoverRunner(Project project) {
        String openCoverPath = settings.getString("sonar.opencover.installDirectory");
        OpenCoverCommand openCoverCommand = new OpenCoverCommand(openCoverPath) ;
        openCoverCommand.setRegister("user");
        
        openCoverCommand.setTargetDir(sonarWorkingDirPath);
        openCoverCommand.setMergeByHash();   

        testEnvironment.setTestsHaveRun();
        openCoverCommand.setOutputPath(testEnvironment.getXmlCoveragePath());
        
        openCoverCommand.setTargetCommand(prepareTestRunner());
        String filter = getFilter();
        openCoverCommand.setFilter(filter); 
        long timeoutMilliseconds = 1000 * 300;
        int exitCode=CommandExecutor.create().execute(openCoverCommand.toCommand(), timeoutMilliseconds);
        if(exitCode != 0) {
            throw new SonarException("OpenCover terminated with exitCode " + exitCode);
        }
    }

    private OpenCoverTarget prepareTestRunner() {
        UnitTestRunner unitTestRunner = UnitTestRunnerFactory.createBasicTestRunnner(propertiesHelper, moduleFileSystem);
        VSTestCommand testCommand=unitTestRunner.prepareTestCommand();
        return testCommand;
    }
    protected String getFilter() {
        final StringBuilder filterBuilder = new StringBuilder();
        // We add all the covered assemblies
        for (String assemblyName : listCoveredAssemblies()) {
          filterBuilder.append("+[" + assemblyName + "]* ");
        }
        return filterBuilder.toString();
    }
    
    protected List<String> listCoveredAssemblies() {
        List<String> coveredAssemblyNames = new ArrayList<String>();
        for (VisualStudioProject visualProject : solution.getProjects()) {
          if (!visualProject.isTest()) {
            coveredAssemblyNames.add(visualProject.getAssemblyName());
          }
        }
        return coveredAssemblyNames;
      }
    private void buildVsTestOpenCoverRunner() {
        throw new NotImplementedException();      
    }

    private void getSolution() {
        solution = getMicrosoftWindowsEnvironment().getCurrentSolution();
        if (solution == null) {
            throw new SonarException("No .NET solution or project has been given to the Gallio command builder.");
        }
    }

    private void ensureWorkDirExists() {
        workDir = new File(solution.getSolutionDir(), getMicrosoftWindowsEnvironment().getWorkingDirectory());
        if (!workDir.exists()) {
          workDir.mkdirs();
        }
    }
    
    private List<File> findTestAssemblies() {
        Set<File> assemblyFiles = Sets.newHashSet();

        Collection<VisualStudioProject> testProjects = solution.getUnitTestProjects();
        for (VisualStudioProject visualStudioProject : testProjects) {
            addAssembly(assemblyFiles, visualStudioProject);
        }

        return Lists.newArrayList(assemblyFiles);
    }
      
      private void addAssembly(Collection<File> assemblyFileList, VisualStudioProject visualStudioProject) {
          String buildConfiguration = settings.getString(DotNetConstants.BUILD_CONFIGURATION_KEY);
          String buildPlatform = settings.getString(DotNetConstants.BUILD_PLATFORM_KEY);
          File assembly = visualStudioProject.getArtifact(buildConfiguration, buildPlatform);
          if (assembly != null && assembly.isFile()) {
            assemblyFileList.add(assembly);
          } else {
            LOG.warn("Test assembly not found at the following location: {}"
              + "\n, using the following configuration:\n  - csproj file: {}\n  - build configuration: {}\n  - platform: {}",
                new Object[] {assembly, visualStudioProject.getProjectFile(), buildConfiguration, buildPlatform});
          }
        }

}
