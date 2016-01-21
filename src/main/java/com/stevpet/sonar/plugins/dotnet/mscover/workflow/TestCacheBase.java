package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import org.sonar.api.BatchExtension;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public abstract class TestCacheBase implements TestCache {

	private File testResults;
	private File testCoverage;
	private boolean hasRun;
	private SonarCoverage sonarCoverage = null;

	public TestCacheBase() {
		super();
	}

	@Override
	public File getTestResults() {
	    return testResults;
	}

	@Override
	public File getTestCoverage() {
	    return testCoverage;
	}

	@Override
	public boolean gatHasRun() {
	    return hasRun;
	}

	@Override public void setHasRun(boolean b) {
		hasRun=b;
	}
	@Override
	public void setHasRun(File coverageFile, File testResultsFile) {
	    this.testCoverage=coverageFile;
	    this.testResults=testResultsFile;
	    hasRun=true;
	}

	@Override
	public TestCache setSonarCoverage(SonarCoverage sonarCoverage) {
		this.sonarCoverage=sonarCoverage ;
		return this;
	}

	@Override
	public SonarCoverage getSonarCoverage() {
		Preconditions.checkState(sonarCoverage != null,"sonarCoverage not set");
		return sonarCoverage;
	}

}