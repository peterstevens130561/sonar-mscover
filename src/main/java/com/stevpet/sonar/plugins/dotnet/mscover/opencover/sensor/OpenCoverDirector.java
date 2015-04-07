package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.ProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.CoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.TestResultsCleaner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageParserStep;

public class OpenCoverDirector {

    private DefaultPicoContainer container;

    public void wire(DefaultPicoContainer container) {
        container.addComponent(new ProcessLock("opencover"))
        .addComponent(LockedWindowsCommandLineExecutor.class)
        .addComponent(VsTestConfigFinder.class)
        .addComponent(WindowsCodeCoverageCommand.class)
        .addComponent(OpenCoverCoverageRunner.class)
        .addComponent(VSTestStdOutParser.class)
        .addComponent(DefaultAssembliesFinder.class)
        .addComponent(VsTestRunnerCommandBuilder.class)
        .addComponent(TestResultsCleaner.class)
        .addComponent(OpenCoverCoverageParser.class)
        .addComponent(VSTestCommand.class)
        .addComponent(InjectingFakesRemover.class);
        this.container=container;
    }
    
    public void execute() {
        TestResultsCleaner testResultsCleaner = container.getComponent(TestResultsCleaner.class);
        testResultsCleaner.execute();
        
        InjectingFakesRemover fakesRemover=container.getComponent(InjectingFakesRemover.class);
        fakesRemover.execute();
        
        VsTestEnvironment testEnvironment = container.getComponent(VsTestEnvironment.class);
        VSTestCommand testCommand = buildTestRunner();
       String stdOut=executeVsTestOpenCoverRunner(testCommand); 
       String resultsPath=getLocationOfTestResultsFile(stdOut);  
       testEnvironment.setTestResultsXmlPath(resultsPath);
       SonarCoverage sonarCoverageRegistry=parseCoverageFile(testEnvironment);
       testEnvironment.setSonarCoverage(sonarCoverageRegistry);
       testEnvironment.setTestsHaveRun();
    }
    
    private VSTestCommand buildTestRunner() {
        VsTestRunnerCommandBuilder unitTestRunner = container.getComponent(VsTestRunnerCommandBuilder.class);
        VSTestCommand testCommand=unitTestRunner.build(false);
        return testCommand;
    }

    private String executeVsTestOpenCoverRunner(VSTestCommand testCommand) {
        OpenCoverCommand openCoverCommand=container.getComponent(OpenCoverCommand.class);
        openCoverCommand.setTargetCommand(testCommand);   
        CoverageRunner runner = container.getComponent(OpenCoverCoverageRunner.class);
        runner.execute();
        return runner.getStdOut();   
    }
    
    private String getLocationOfTestResultsFile(String stdOut) {
        VSTestStdOutParser vsTestStdOutParser = container.getComponent(VSTestStdOutParser.class);
        vsTestStdOutParser.setStdOut(stdOut);
        return vsTestStdOutParser.getTestResultsXmlPath(); 

    }
    
    private SonarCoverage parseCoverageFile(VsTestEnvironment testEnvironment) {
        SonarCoverage sonarCoverageRegistry = new SonarCoverage();

        CoverageParserStep parser = container.getComponent(CoverageParserStep.class);
        parser.parse(sonarCoverageRegistry,new File(testEnvironment.getXmlCoveragePath()));
        return sonarCoverageRegistry;
    }

}
