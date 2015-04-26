package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.DefaultBranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.DefaultCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.DefaultLineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.NullCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.ProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.DefaultTestResultsFormatter;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.DefaultTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.DefaultTestResultsParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.OpenCoverFileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.VsTestCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder.VsTestFileNamesParser;

public class VsTestWorkflowSteps implements WorkflowSteps {

	@Override
	public Class<? extends TestRunner> getTestRunner() {
		return  WindowsVsTestRunner.class;
	}


	@Override
	public Class< ? extends CoverageParserStep> getCoverageParser() {
		return VsTestCoverageParser.class;
	}


	@Override
	public Class<? extends TestResultsBuilder> getTestResultsParser() {
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
        .addComponent(DefaultBranchFileCoverageSaver.class);
        addTestResultsBuilderComponents(container);
	}
	
	private void addTestResultsBuilderComponents(DefaultPicoContainer container) {
		container
		.addComponent(VsTestFileNamesParser.class)
		.addComponent(DefaultTestResultsParser.class);
	}
}
