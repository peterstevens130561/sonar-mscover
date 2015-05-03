package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.NullCoverageSaver;


public class NullWorkflowSteps implements WorkflowSteps {

	@Override
	public Class<? extends TestRunner> getTestRunner() {
		return NullTestRunner.class;
	}

	@Override
	public Class<? extends CoverageReader>  getCoverageReader() {

		return NullCoverageReader.class;
	}

	@Override
	public Class<? extends TestResultsBuilder>  getTestResultsBuilder() {

		return NullTestResultsBuilder.class;
	}

	@Override
	public Class<? extends CoverageSaver> getCoverageSaver() {
		return NullCoverageSaver.class;
	}

	@Override
	public Class<? extends TestResultsSaver> getTestResultsSaver() {
		return NullTestResultsSaver.class;
	}

	@Override
	public void getComponents(DefaultPicoContainer picoContainer) {

	}

}
