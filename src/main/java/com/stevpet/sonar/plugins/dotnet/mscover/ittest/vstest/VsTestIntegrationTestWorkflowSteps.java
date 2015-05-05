package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.nullsaver.NullBranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.VsTestCoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.nullrestrunner.NullTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.VsTestFilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.NullTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.NullTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowSteps;

public class VsTestIntegrationTestWorkflowSteps implements WorkflowSteps {

	@Override
	public Class<? extends TestRunner> getTestRunner() {
		return NullTestRunner.class;
	}

	@Override
	public Class<? extends CoverageReader> getCoverageReader() {
		return IntegrationTestCoverageReader.class;
	}

	@Override
	public Class<? extends TestResultsBuilder> getTestResultsBuilder() {
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
		getCoverageReaderComponents(container);
		getCoverageSaverComponents(container);
	}
	
	private void getCoverageSaverComponents(DefaultPicoContainer container) {
        container
		.addComponent(IntegrationTestLineFileCoverageSaver.class)
        .addComponent(NullBranchFileCoverageSaver.class);	
	}

	private void getCoverageReaderComponents(DefaultPicoContainer container) {
		container
        .addComponent(WindowsCommandLineExecutor.class)
        .addComponent(VsTestFilteringCoverageParser.class)
        .addComponent(WindowsCodeCoverageCommand.class)
        .addComponent(VsTestCoverageToXmlConverter.class);
	}	
	
}
