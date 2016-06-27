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
	private String projectName = "bogus";
	private File root = new File("bogus");
    @Test
	public void simpleTest() throws FileNotFoundException, XMLStreamException, TransformerException {
		File testCoverageFile = TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
		CoverageModuleSaver coverageModuleSaver = mock(CoverageModuleSaver.class);
		new OpenCoverModuleSplitter(coverageModuleSaver,coverageHashes).splitCoverageFileInFilePerModule(root, projectName, testCoverageFile);
		verify(coverageModuleSaver,times(34)).save(anyString());
	}
	
	@Test
	public void fullTest() throws FileNotFoundException, XMLStreamException, TransformerException {
		File testCoverageFile = TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
		ModuleParser moduleParser = new OpenCoverModuleParser();
		File tempDir = Files.createTempDir();
		
		OpenCoverCoverageModuleSaver moduleLambda = new OpenCoverCoverageModuleSaver(moduleParser);
		moduleLambda.setDirectory(tempDir);
		moduleLambda.setProject("BaseProject");
		new OpenCoverModuleSplitter(moduleLambda,coverageHashes).splitCoverageFileInFilePerModule(root, projectName, testCoverageFile);
	}

}