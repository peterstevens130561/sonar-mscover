package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ModuleSaverLambdaTest {

	@Mock private ModuleParser parser;
	ModuleSaverLambda moduleSaverLambda ;
	
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		moduleSaverLambda = new ModuleSaverLambda(parser);
	}
	
	@Test
	public void testOneDot() {
		File coverageFile=moduleSaverLambda.setDirectory(new File("c:/root")).setProject("projectName").getArtifactCoverageFile("hi.dll");
		assertEquals(new File("c:/root/hi/projectName.xml"),coverageFile);
	}
	
	@Test
	public void testTwoDots() {
		File coverageFile=moduleSaverLambda.setDirectory(new File("c:/root")).setProject("projectName").getArtifactCoverageFile("hi.john.dll");
		assertEquals(new File("c:/root/hi.john/projectName.xml"),coverageFile);
	}
	
	@Test
	public void testNoDots() {
		File coverageFile=moduleSaverLambda.setDirectory(new File("c:/root")).setProject("projectName").getArtifactCoverageFile("assemblyWithNoDots");
		assertEquals(new File("c:/root/assemblyWithNoDots/projectName.xml"),coverageFile);
	}
}
