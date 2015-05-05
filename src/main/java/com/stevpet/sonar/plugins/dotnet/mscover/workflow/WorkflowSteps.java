package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;

/**
 * workflow deals with a specific usecase / scenario of the plugin. For each workflow a Null object exists
 *
 */
public interface WorkflowSteps {
	
	Class<? extends TestRunner> getTestRunner();
	Class<? extends CoverageReader> getCoverageReader();
	Class<? extends TestResultsBuilder> getTestResultsBuilder();
	Class<? extends CoverageSaver> getCoverageSaver();
	Class<? extends TestResultsSaver> getTestResultsSaver();
	
	/**
	 * The steps may need specific components, add these here
	 * @param picoContainer - to add the components to
	 */
	void getComponents(DefaultPicoContainer picoContainer);
	
}
