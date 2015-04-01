package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.ProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.TestResultsCleaner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;

public class OpenCoverDirector {

    private DefaultPicoContainer container;
    public void wire(DefaultPicoContainer container) {
        container.addComponent(new ProcessLock("opencover"))
        .addComponent(LockedWindowsCommandLineExecutor.class)
        .addComponent(WindowsVsTestRunner.class)
        .addComponent(VsTestConfigFinder.class)
        .addComponent(WindowsCodeCoverageCommand.class)
        .addComponent(OpenCoverCoverageRunner.class)
        .addComponent(VSTestStdOutParser.class)
        .addComponent(DefaultAssembliesFinder.class)
        .addComponent(VsTestRunnerCommandBuilder.class)
        .addComponent(TestResultsCleaner.class)
        .addComponent(OpenCoverCoverageParser.class)
        .addComponent(VSTestCommand.class);
        this.container=container;
    }
    
    

}
