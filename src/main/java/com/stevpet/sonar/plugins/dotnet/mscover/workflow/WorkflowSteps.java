package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;

/**
 * workflow deals with a specific usecase / scenario of the plugin. For each workflow a Null object exists
 *
 */
public interface WorkflowSteps {
	
	public Class<TestRunnerStep> getTestRunner();
	public Class<CoverageParserStep> getCoverageParser();
	public Class<TestResultsBuilder> getTestResultsParser();
	public Class<CoverageSaver> getCoverageSaver();
	public Class<TestResultsSaver> getTestResultsSaver();
	
	/**
	 * The steps may need specific components, add these here
	 * @param picoContainer - to add the components to
	 */
	public void getComponents(DefaultPicoContainer picoContainer);
	
}
