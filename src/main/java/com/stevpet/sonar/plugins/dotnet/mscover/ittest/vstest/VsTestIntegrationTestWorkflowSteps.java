package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.DefaultCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.NullBranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.VsTestCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageReaderStep;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.NullTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.NullTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.NullTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowSteps;

public class VsTestIntegrationTestWorkflowSteps implements WorkflowSteps {

	@Override
	public Class<? extends TestRunner> getTestRunner() {
		return NullTestRunner.class;
	}

	@Override
	public Class<? extends CoverageReaderStep> getCoverageReader() {
		return IntegrationTestCoverageReader.class;
	}

	@Override
	public Class<? extends TestResultsBuilder> getTestResultsParser() {
		return NullTestResultsBuilder.class;
	}

	@Override
	public Class<? extends CoverageSaver> getCoverageSaver() {
		return DefaultCoverageSaver.class;
	}

	@Override
	public Class<? extends TestResultsSaver> getTestResultsSaver() {
		return NullTestResultsSaver.class;
	}

	@Override
	public void getComponents(DefaultPicoContainer container) {
		getCoverageParserComponents(container);
		getCoverageSaverComponents(container);
	}
	
	private void getCoverageSaverComponents(DefaultPicoContainer container) {
        container
		.addComponent(IntegrationTestLineFileCoverageSaver.class)
        .addComponent(NullBranchFileCoverageSaver.class);	
	}

	private void getCoverageParserComponents(DefaultPicoContainer container) {
		container
        .addComponent(WindowsCommandLineExecutor.class)
        .addComponent(VsTestCoverageParser.class)
        .addComponent(WindowsCodeCoverageCommand.class);
	}	
	
}
