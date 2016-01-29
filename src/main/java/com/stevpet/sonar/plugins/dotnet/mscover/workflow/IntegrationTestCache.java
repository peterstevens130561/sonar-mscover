package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import javax.annotation.Nonnull;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

/**
 * Should be used by all integration test sensors. persists during a batch
 * @author stevpet
 *
 */
@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class IntegrationTestCache extends TestCacheBase implements BatchExtension{

	private ProjectUnitTestResults testResults;

	
	/**
	 * get the first analysed module
	 * @return
	 */

	public void setTestResults(ProjectUnitTestResults testResults) {
		this.testResults=testResults;
	}
	
	public ProjectUnitTestResults getTestResults() {
		return this.testResults ;
	}
    
}