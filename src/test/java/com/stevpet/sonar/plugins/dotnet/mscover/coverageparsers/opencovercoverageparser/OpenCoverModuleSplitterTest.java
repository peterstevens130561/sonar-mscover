package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.codehaus.plexus.util.FileUtils;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.google.common.io.Files;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesplitter.ModuleSaverLambda;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesplitter.ModuleLambda;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesplitter.ModuleParser;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesplitter.OpenCoverModuleParser;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesplitter.OpenCoverModuleSplitter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyString;

public class OpenCoverModuleSplitterTest {

	@Test
	public void simpleTest() throws FileNotFoundException, XMLStreamException, TransformerException {
		File xmlFile = TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
		ModuleLambda moduleHelper = mock(ModuleLambda.class);
		int modules=new OpenCoverModuleSplitter(moduleHelper).splitFile(xmlFile);
		verify(moduleHelper,times(34)).execute(anyString());
	}
	
	@Test
	public void fullTest() throws FileNotFoundException, XMLStreamException, TransformerException {
		File xmlFile = TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
		ModuleParser moduleParser = new OpenCoverModuleParser();
		File tempDir = Files.createTempDir();
		
		ModuleSaverLambda moduleLambda = new ModuleSaverLambda(moduleParser);
		moduleLambda.setDirectory(tempDir);
		moduleLambda.setProject("BaseProject");
		int modules=new OpenCoverModuleSplitter(moduleLambda).splitFile(xmlFile);	
	}

}