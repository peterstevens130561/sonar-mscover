package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.google.common.io.Files;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.CoverageModuleSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.ModuleParser;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.OpenCoverCoverageModuleSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.OpenCoverModuleParser;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.OpenCoverModuleSplitter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyString;

public class OpenCoverModuleSplitterTest {

	private CoverageHashes coverageHashes = new CoverageHashes();

    @Test
	public void simpleTest() throws FileNotFoundException, XMLStreamException, TransformerException {
		File xmlFile = TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
		CoverageModuleSaver moduleHelper = mock(CoverageModuleSaver.class);
		int modules=new OpenCoverModuleSplitter(moduleHelper,coverageHashes).splitFile(xmlFile);
		verify(moduleHelper,times(34)).save(anyString());
	}
	
	@Test
	public void fullTest() throws FileNotFoundException, XMLStreamException, TransformerException {
		File xmlFile = TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
		ModuleParser moduleParser = new OpenCoverModuleParser();
		File tempDir = Files.createTempDir();
		
		OpenCoverCoverageModuleSaver moduleLambda = new OpenCoverCoverageModuleSaver(moduleParser);
		moduleLambda.setDirectory(tempDir);
		moduleLambda.setProject("BaseProject");
		int modules=new OpenCoverModuleSplitter(moduleLambda,coverageHashes).splitFile(xmlFile);	
	}

}