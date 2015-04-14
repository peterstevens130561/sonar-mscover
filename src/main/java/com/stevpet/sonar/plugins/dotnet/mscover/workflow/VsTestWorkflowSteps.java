package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.NullCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;

public class VsTestWorkflowSteps implements WorkflowSteps {

	@Override
	public Class getTestRunner() {
		return  WindowsVsTestRunner.class;
	}


	@Override
	public Class getCoverageParser() {
		return NullCoverageParser.class;
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
	public void getComponents(DefaultPicoContainer picoContainer) {
	}
}
