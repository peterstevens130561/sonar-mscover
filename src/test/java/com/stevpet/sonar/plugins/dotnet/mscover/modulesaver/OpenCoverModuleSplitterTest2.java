package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.test.TestUtils;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class OpenCoverModuleSplitterTest2 {

    private OpenCoverModuleSplitter moduleSplitter;
    private CoverageFileLocator coverageFileLocator = new DefaultCoverageFileLocator();
    @Mock
	private CoverageHashes coverageHashes;
    
    @Before()
public void before(){
        org.mockito.MockitoAnnotations.initMocks(this);
        when(coverageHashes.add(anyString())).thenReturn(false);
        moduleSplitter= new OpenCoverModuleSplitter(coverageHashes);
    }

    @Test
	public void basicTest() throws XMLStreamException, TransformerException, IOException {
		File xmlFile = TestUtils.getResource("OpenCoverModulesSplitter/twomodules.xml");
		Path rootPath = Files.createTempDirectory("cover");
		File root = rootPath.toFile();
		moduleSplitter.splitCoverageFileInFilePerModule(root, "ProjectName", xmlFile);
		File firstXmlFile = new File(root,"Microsoft.VisualStudio.QualityTools.Resource/ProjectName.xml");
		assertFalse("File should not exist as it is empty" + firstXmlFile.getAbsolutePath(),firstXmlFile.exists());
		File secondXmlFile = new File(root,"joaGridder3DAddin/ProjectName.xml");
		assertTrue("File should exist " + secondXmlFile.getAbsolutePath(),secondXmlFile.exists());
		
		// if we get here, remove the tempdir
		FileUtils.deleteDirectory(root);
	}

	@Test
	public void testProjectt() throws XMLStreamException, TransformerException, IOException {
		File xmlFile = TestUtils.getResource("OpenCoverModulesSplitter/twomodules.xml");
		Path rootPath = Files.createTempDirectory("cover");
		File root = rootPath.toFile();
	    String testProjectName = "joaGridder3DAddin";
	    moduleSplitter.splitCoverageFileInFilePerModule(root, testProjectName, xmlFile);
		File coverageFile= coverageFileLocator.getFile(root,testProjectName,testProjectName);
		
		assertNotNull(coverageFile);
		assertTrue("coverage file of this project should exist",coverageFile.exists());
	}

}
