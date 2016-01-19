package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class DefaultUnitTestCache implements BatchExtension, TestCache {
    private File testResults ;
    private File testCoverage ;
    private boolean hasRun;
	private SonarCoverage sonarCoverage=null;
    
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestCache#getTestResults()
	 */
    @Override
	public File getTestResults() {
        return testResults;
    }

    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestCache#getTestCoverage()
	 */
    @Override
	public File getTestCoverage() {
        return testCoverage;
    }

    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestCache#hasRun()
	 */
    @Override
	public boolean hasRun() {
        return hasRun;
    }
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestCache#setHasRun(java.io.File, java.io.File)
	 */
    @Override
	public void setHasRun(File coverageFile, File testResultsFile) {
        this.testCoverage=coverageFile;
        this.testResults=testResultsFile;
        hasRun=true;
    }
    
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestCache#setSonarCoverage(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage)
	 */
    @Override
	public  TestCache setSonarCoverage(SonarCoverage sonarCoverage) {
    	this.sonarCoverage=sonarCoverage ;
    	return this;
    }
 
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestCache#getSonarCoverage()
	 */
    @Override
	public SonarCoverage getSonarCoverage() {
		Preconditions.checkState(sonarCoverage != null,"sonarCoverage not set");
    	return sonarCoverage;
    }
    
}
