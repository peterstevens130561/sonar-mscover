package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.ProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.InjectingFakesRemover;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.TestResultsCleaner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestRunner;

public class VsTestExecutionSensorDirector {

    private DefaultPicoContainer container ;
    public void wire(DefaultPicoContainer container) {
        this.container = container;
        container.addComponent(new ProcessLock("vstest.console.exe"))
        .addComponent(LockedWindowsCommandLineExecutor.class)
        .addComponent(TestResultsCleaner.class)
        .addComponent(DefaultAssembliesFinder.class)
        .addComponent(VSTestCommand.class)
        .addComponent(VsTestRunnerCommandBuilder.class)
        .addComponent(VSTestStdOutParser.class)
        .addComponent(VsTestConfigFinder.class)
        .addComponent(WindowsCodeCoverageCommand.class)
        .addComponent(WindowsVsTestRunner.class);
    }

    public void execute() {
        TestResultsCleaner testResultsCleaner = container.getComponent(TestResultsCleaner.class);
        testResultsCleaner.execute();
        
        InjectingFakesRemover fakesRemover=container.getComponent(InjectingFakesRemover.class);
        fakesRemover.execute();
        
        VsTestEnvironment testEnvironment = container.getComponent(VsTestEnvironment.class);
 
        TestRunner runner = container.getComponent(TestRunner.class);
        runner.execute();
        testEnvironment.setTestResultsFile(runner.getTestResultsFile());

    }
}
