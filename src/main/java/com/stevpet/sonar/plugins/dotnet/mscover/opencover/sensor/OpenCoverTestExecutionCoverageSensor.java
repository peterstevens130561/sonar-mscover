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

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverTarget;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.ConcreteOpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.ProjectSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.SonarProjectSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AbstractVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultVsTestRunnerFactory;
@DependsUpon(DotNetConstants.CORE_PLUGIN_EXECUTED)
@DependedUpon("OpenCoverRunningVsTest")
public class OpenCoverTestExecutionCoverageSensor extends AbstractDotNetSensor {

    private static String WONT_EXECUTE = "VsTest.Console using OpenCover.Console.Exe won't execute as ";
    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverTestExecutionCoverageSensor.class);
    private VisualStudioSolution solution;
    private File workDir;
    private final MsCoverProperties propertiesHelper ;
    private final ModuleFileSystem moduleFileSystem;
    private VsTestEnvironment testEnvironment;
    private CommandLineExecutor commandLineExecutor = new WindowsCommandLineExecutor();
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private VsTestRunner unitTestRunner;
    private OpenCoverCommand openCoverCommand = new OpenCoverCommand();
    private AbstractVsTestRunnerFactory vsTestRunnerFactory = new DefaultVsTestRunnerFactory();
    private OpenCoverCommandBuilder openCoverCommandBuilder = new OpenCoverCommandBuilder();
    private AssembliesFinderFactory assembliesFinderFactory = new AssembliesFinderFactory();
    private VSTestStdOutParser vsTestStdOutParser = new VSTestStdOutParser();
    private OpenCoverParserFactory openCoverParserFactory = new ConcreteOpenCoverParserFactory();
    
    public OpenCoverTestExecutionCoverageSensor(MsCoverProperties propertiesHelper, 
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            ModuleFileSystem moduleFileSystem,
            VsTestEnvironment testEnvironment) {
        super(microsoftWindowsEnvironment, "OpenCover", propertiesHelper.getMode());
        this.propertiesHelper = propertiesHelper;
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
        boolean isRoot=project.isRoot();
        String language=project.getLanguageKey();
        if (isRoot || !"cs".equals(language)) {
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
        /* projectSeam.setProject(project);
        File opencoverCoverageFile= projectSeam.getSonarFile("coverage-report.xml");
        openCoverCoveragePath= opencoverCoverageFile.getAbsolutePath();
        testEnvironment.setCoverageXmlPath(openCoverCoveragePath);
        */
        testEnvironment.setCoverageXmlFile(project,"coverage-report.xml");
        unitTestRunner = vsTestRunnerFactory.createBasicTestRunnner(propertiesHelper, moduleFileSystem,microsoftWindowsEnvironment);
        String openCoverPath = propertiesHelper.getOpenCoverInstallPath();
        openCoverCommand.setCommandPath(openCoverPath);
        
        getSolution();
        ensureWorkDirExists();
        
        executeVsTestOpenCoverRunner();
        getResultPaths();
        // tell that tests were executed so that no other project tries to launch them a second time
        getMicrosoftWindowsEnvironment().setTestExecutionDone();
        parseCoverageFile(testEnvironment);
    }

    private void parseCoverageFile(VsTestEnvironment testEnvironment) {
        SonarCoverage sonarCoverageRegistry = new SonarCoverage();
        Collection<String> pdbsThatCanBeIgnoredWhenMissing = propertiesHelper.getPdbsThatMayBeIgnoredWhenMissing();
        XmlParserSubject parser=openCoverParserFactory.createOpenCoverParser(sonarCoverageRegistry,pdbsThatCanBeIgnoredWhenMissing);
        parser.parseFile(new File(testEnvironment.getXmlCoveragePath()));
        testEnvironment.setSonarCoverage(sonarCoverageRegistry);
    }

    
    private void executeVsTestOpenCoverRunner() {
        unitTestRunner.clean();

        openCoverCommandBuilder.setOpenCoverCommand(openCoverCommand);
        openCoverCommandBuilder.setSolution(solution);
        openCoverCommandBuilder.setMsCoverProperties(propertiesHelper);
        openCoverCommandBuilder.setTestRunner(unitTestRunner);
        openCoverCommandBuilder.setTestEnvironment(testEnvironment);
        openCoverCommandBuilder.build();
        
        AssembliesFinder finder = assembliesFinderFactory.create(propertiesHelper);
        String targetDir=finder.findUnitTestAssembliesDir(solution);
        openCoverCommand.setTargetDir(targetDir);
        
        commandLineExecutor.execute(openCoverCommand);
        testEnvironment.setTestsHaveRun();
    }
    
    
    /**
     * parse test log to get paths to result files
     */
    public void getResultPaths() {

        String stdOut=commandLineExecutor.getStdOut();
        vsTestStdOutParser.setResults(stdOut);
        String resultsPath=vsTestStdOutParser.getTestResultsXmlPath(); 
        testEnvironment.setTestResultsXmlPath(resultsPath);
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

    /**
     * @param vsTestRunnerFactory the vsTestRunnerFactory to set
     */
    public void setVsTestRunnerFactory(
            AbstractVsTestRunnerFactory vsTestRunnerFactory) {
        this.vsTestRunnerFactory = vsTestRunnerFactory;
    }

    public void setOpenCoverCommand(OpenCoverCommand openCoverCommand) {
       this.openCoverCommand = openCoverCommand;
    }
    /**
     * @param projectSeam the projectSeam to set
     */
    public void setProjectSeam(ProjectSeam projectSeam) {
        //this.projectSeam = projectSeam;
    }

    public void setOpenCoverCommandBuilder(OpenCoverCommandBuilder mock) {
        this.openCoverCommandBuilder=mock;
        
    }

    public void setAssembliesFinderFactory(AssembliesFinderFactory assembliesFinderFactory) {
        this.assembliesFinderFactory=assembliesFinderFactory;
    }
 
    public void setCommandLineExecutor(CommandLineExecutor commandLineExecutor) {
        this.commandLineExecutor = commandLineExecutor;
    }

    /**
     * @param vsTestStdOutParser the vsTestStdOutParser to set
     */
    public void setVsTestStdOutParser(VSTestStdOutParser vsTestStdOutParser) {
        this.vsTestStdOutParser = vsTestStdOutParser;
    }

    /**
     * @param openCoverParserFactory the openCoverParserFactory to set
     */
    public void setOpenCoverParserFactory(
            OpenCoverParserFactory openCoverParserFactory) {
        this.openCoverParserFactory = openCoverParserFactory;
    }


}
