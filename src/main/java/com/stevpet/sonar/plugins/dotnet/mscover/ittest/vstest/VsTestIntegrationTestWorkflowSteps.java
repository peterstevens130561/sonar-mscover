package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageParserStep;
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
	public Class<? extends CoverageParserStep> getCoverageParser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends TestResultsBuilder> getTestResultsParser() {
		return NullTestResultsBuilder.class;
	}

	@Override
	public Class<? extends CoverageSaver> getCoverageSaver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends TestResultsSaver> getTestResultsSaver() {
		return NullTestResultsSaver.class;
	}

	@Override
	public void getComponents(DefaultPicoContainer picoContainer) {
		// TODO Auto-generated method stub

	}

}
