package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.DotNetConstants;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;
import org.sonar.plugins.dotnet.api.sensor.AbstractDotNetSensor;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverTarget;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.ConcreteOpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerFactory;
@DependsUpon(DotNetConstants.CORE_PLUGIN_EXECUTED)
@DependedUpon("OpenCoverRunningVsTest")
public class OpenCoverTestExecutionCoverageSensor extends AbstractDotNetSensor {

    private static String WONT_EXECUTE = "VsTest.Console using OpenCover.Console.Exe won't execute as ";
    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverTestExecutionCoverageSensor.class);
    private VisualStudioSolution solution;
    private File workDir;
    private PropertiesHelper propertiesHelper ;
    private final Settings settings;
    private ModuleFileSystem moduleFileSystem;
    private String openCoverCoveragePath;
    private VsTestEnvironment testEnvironment;
    private String sonarWorkingDirPath;
    WindowsCommandLineExecutor commandLineExecutor = new WindowsCommandLineExecutor();
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private VsTestRunner unitTestRunner;
    private OpenCoverCommand openCoverCommand;

    
    public OpenCoverTestExecutionCoverageSensor(Settings settings, 
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            ModuleFileSystem moduleFileSystem,
            VsTestEnvironment testEnvironment) {
        super(microsoftWindowsEnvironment, "OpenCover", PropertiesHelper.MSCOVER_MODE);
        propertiesHelper = PropertiesHelper.create(settings);
        this.settings = settings;
        this.moduleFileSystem = moduleFileSystem;
        this.testEnvironment = testEnvironment;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
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
        openCoverCoveragePath= sonarWorkingDirPath + "\\coverage-report.xml";
        testEnvironment.setCoverageXmlPath(openCoverCoveragePath);
        unitTestRunner = VsTestRunnerFactory.createBasicTestRunnner(propertiesHelper, moduleFileSystem,microsoftWindowsEnvironment);
        String openCoverPath = settings.getString("sonar.opencover.installDirectory");
        openCoverCommand = new OpenCoverCommand(openCoverPath);
        
        getSolution();
        ensureWorkDirExists();
        
        executeVsTestOpenCoverRunner(project);
        getResultPaths();
        // tell that tests were executed so that no other project tries to launch them a second time
        getMicrosoftWindowsEnvironment().setTestExecutionDone();
        parseCoverageFile(testEnvironment);
        
     
    }

    private void parseCoverageFile(VsTestEnvironment testEnvironment) {
        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        SonarCoverage sonarCoverageRegistry = new SonarCoverage();
        Collection<String> pdbsThatCanBeIgnoredWhenMissing = propertiesHelper.getPdbsThatMayBeIgnoredWhenMissing();
        XmlParserSubject parser=parserFactory.createOpenCoverParser(sonarCoverageRegistry,pdbsThatCanBeIgnoredWhenMissing);
        parser.parseFile(new File(testEnvironment.getXmlCoveragePath()));
        testEnvironment.setSonarCoverage(sonarCoverageRegistry);
    }

    
    private void executeVsTestOpenCoverRunner(Project project) {

        openCoverCommand.setRegister("user");
        
        openCoverCommand.setTargetDir(sonarWorkingDirPath);
        openCoverCommand.setMergeByHash();   

        List<String> excludeFilters = new ArrayList<String>();
        excludeFilters.add("*\\*.Designer.cs");
        openCoverCommand.setExcludeByFileFilter(excludeFilters);
        openCoverCommand.setExcludeFromCodeCoverageAttributeFilter();
        testEnvironment.setTestsHaveRun();
        openCoverCommand.setOutputPath(testEnvironment.getXmlCoveragePath());
        
        openCoverCommand.setTargetCommand(prepareTestRunner());
        String filter = getAssembliesToIncludeInCoverageFilter();
        openCoverCommand.setFilter(filter); 
        unitTestRunner.clean();
        commandLineExecutor.execute(openCoverCommand);
    }
    
    
    /**
     * parse test log to get paths to result files
     */
    public void getResultPaths() {
        VSTestStdOutParser vsTestResults = new VSTestStdOutParser();
        String stdOut=commandLineExecutor.getStdOut();
        vsTestResults.setResults(stdOut);
        String resultsPath=vsTestResults.getTestResultsXmlPath(); 
        testEnvironment.setTestResultsXmlPath(resultsPath);
    }

    private OpenCoverTarget prepareTestRunner() {

        VSTestCommand testCommand=unitTestRunner.prepareTestCommand();
        return testCommand;
    }
    protected String getAssembliesToIncludeInCoverageFilter() {
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
            coveredAssemblyNames.add(visualProject.getAssemblyName());
        }
        return coveredAssemblyNames;
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
    

}
