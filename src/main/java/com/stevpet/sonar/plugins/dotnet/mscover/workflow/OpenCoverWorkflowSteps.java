package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.NullCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.ProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.InjectingFakesRemover;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.TestResultsCleaner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerCommandBuilder;

public class OpenCoverWorkflowSteps implements WorkflowSteps {

	@Override
	public Class getTestRunner() {
		return OpenCoverCoverageRunner.class;
	}


	@Override
	public Class getCoverageParser() {
		return OpenCoverCoverageParser.class;
	}


	@Override
	public Class getTestResultsParser() {
		return NullTestResultsBuilder.class;
	}


	@Override
	public Class getCoverageSaver() {
		return NullCoverageSaver.class;
	}


	@Override
	public Class getTestResultsSaver() {
		return NullTestResultsSaver.class;
	}
	
	@Override
	public void getComponents(DefaultPicoContainer container) {
		getTestRunnerComponents(container);
	}
	

	private void getTestRunnerComponents(DefaultPicoContainer container) {
        container.addComponent(new ProcessLock("opencover"))
        .addComponent(LockedWindowsCommandLineExecutor.class)
        .addComponent(VsTestConfigFinder.class)
        .addComponent(WindowsCodeCoverageCommand.class)
        .addComponent(VSTestStdOutParser.class)
        .addComponent(DefaultAssembliesFinder.class)
        .addComponent(VsTestRunnerCommandBuilder.class)
        .addComponent(VSTestCommand.class);	
	}

}
