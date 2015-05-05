package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultBranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultLineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.DefaultTestResultsFormatter;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.DefaultTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.DefaultTestResultsParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.OpenCoverFileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;

public class OpenCoverWorkflowSteps implements WorkflowSteps {

	@Override
	public Class<? extends TestRunner> getTestRunner() {
		return OpenCoverCoverageRunner.class;
	}


	@Override
	public Class< ? extends CoverageReader> getCoverageReader() {
		return DefaultCoverageReader.class;
	}


	@Override
	public Class<? extends TestResultsBuilder> getTestResultsBuilder() {
		return DefaultTestResultsBuilder.class;
	}


	@Override
	public Class<? extends CoverageSaver> getCoverageSaver() {
		return DefaultCoverageSaver.class;
	}


	@Override
	public Class<? extends TestResultsSaver> getTestResultsSaver() {
		return DefaultTestResultsSaver.class;
	}
	
	@Override
	public void getComponents(DefaultPicoContainer container) {
		getTestRunnerComponents(container);
		getTestResultsSaverComponents(container);
	}
	
	private void getTestResultsSaverComponents(DefaultPicoContainer container) {
		container.addComponent(DefaultResourceResolver.class)
		.addComponent(DefaultTestResultsFormatter.class);
	}


	private void getTestRunnerComponents(DefaultPicoContainer container) {
        container.addComponent(new ProcessLock("opencover"))
        .addComponent(OpenCoverCommand.class)
        .addComponent(LockedWindowsCommandLineExecutor.class)
        .addComponent(VsTestConfigFinder.class)
        .addComponent(WindowsCodeCoverageCommand.class)
        .addComponent(VSTestStdOutParser.class)
        .addComponent(DefaultAssembliesFinder.class)
        .addComponent(VsTestRunnerCommandBuilder.class)
        .addComponent(VSTestCommand.class)
        .addComponent(DefaultLineFileCoverageSaver.class)
        .addComponent(DefaultBranchFileCoverageSaver.class)
        .addComponent(OpenCoverCoverageParser.class);
        addTestResultsBuilderComponents(container);
	}
	
	private void addTestResultsBuilderComponents(DefaultPicoContainer container) {
		container
		.addComponent(OpenCoverFileNamesParser.class)
		.addComponent(DefaultTestResultsParser.class);
	}

}
