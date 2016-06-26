package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class CoverageFileLocatorTest {

	@Mock private ModuleParser parser;
	CoverageFileLocator coverageFileLocator ;
	
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		coverageFileLocator = new DefaultCoverageFileLocator();
	}
	
	@Test
	public void testOneDot() {
		File coverageFile=coverageFileLocator.getFile(new File("c:/root"),"projectName","hi.dll");
		assertEquals(new File("c:/root/hi/projectName.xml"),coverageFile);
	}
	
	@Test
	public void testTwoDots() {
		File coverageFile=coverageFileLocator.getFile(new File("c:/root"),"projectName","hi.john.dll");
		assertEquals(new File("c:/root/hi.john/projectName.xml"),coverageFile);
	}
	
	@Test
	public void testNoDots() {
		File coverageFile=coverageFileLocator.getFile(new File("c:/root"),"projectName","assemblyWithNoDots");
		assertEquals(new File("c:/root/assemblyWithNoDots/projectName.xml"),coverageFile);
	}
}