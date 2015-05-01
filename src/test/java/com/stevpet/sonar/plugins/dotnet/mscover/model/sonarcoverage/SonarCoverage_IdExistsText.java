package com.stevpet.sonar.plugins.dotnet.mscover.model.sonarcoverage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class SonarCoverage_IdExistsText {

	private SonarCoverage sonarCoverage ;
	
	@Before()
	public void before() {
		sonarCoverage = new SonarCoverage();
	}
	
	@Test
	public void emptyList_shouldNotExist() {
		boolean exist=sonarCoverage.fileIdExists("0");
		assertFalse("should not exist",exist);
	}
	
	@Test
	public void oneItem_shouldExist() {
		sonarCoverage.getCoveredFile("1234");
		boolean exist=sonarCoverage.fileIdExists("1234");
		assertTrue("inserted, requested same so should exist",exist);
	}
	
	@Test
	public void oneItem_findOthershouldNotExist() {
		sonarCoverage.getCoveredFile("1234");
		boolean exist=sonarCoverage.fileIdExists("1238");
		assertFalse("inserted, requested other so should not exist",exist);
	}
}
