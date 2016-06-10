package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.test.TestUtils;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class OpenCoverModuleSaverTest {

    private OpenCoverModuleSplitter moduleSplitter;
    private CoverageFileLocator coverageFileLocator = new DefaultCoverageFileLocator();
    @Mock
	private CoverageHashes coverageHashes;
    
    @Before()
public void before(){
        org.mockito.MockitoAnnotations.initMocks(this);
        when(coverageHashes.add(anyString())).thenReturn(false);
    }

    @Test
	public void basicTest() throws XMLStreamException, TransformerException, IOException {
		File xmlFile = TestUtils.getResource("OpenCoverModulesSplitter/twomodules.xml");
	    moduleSplitter= new OpenCoverModuleSplitter(coverageHashes);
		Path rootPath = Files.createTempDirectory("cover");
		File root = rootPath.toFile();
		moduleSplitter.setRoot(root).setProject("ProjectName").splitFile(xmlFile);
		File firstXmlFile = new File(root,"Microsoft.VisualStudio.QualityTools.Resource/ProjectName.xml");
		assertFalse("File should not exist as it is empty" + firstXmlFile.getAbsolutePath(),firstXmlFile.exists());
		File secondXmlFile = new File(root,"joaGridder3DAddin/ProjectName.xml");
		assertTrue("File should exist " + secondXmlFile.getAbsolutePath(),secondXmlFile.exists());
		
		// if we get here, remove the tempdir
		Path path =  Paths.get(root.getAbsolutePath());
		FileUtils.deleteDirectory(root);
	}

	@Test
	public void testProjectt() throws XMLStreamException, TransformerException, IOException {
		File xmlFile = TestUtils.getResource("OpenCoverModulesSplitter/twomodules.xml");
		OpenCoverModuleSplitter moduleSaver= new OpenCoverModuleSplitter(coverageHashes);
		Path rootPath = Files.createTempDirectory("cover");
		File root = rootPath.toFile();
		moduleSaver.setRoot(root).setProject("joaGridder3DAddin").splitFile(xmlFile);
		//File coverageFile=moduleSaver.getCoverageFile("joaGridder3DAddin");
		File coverageFile= coverageFileLocator.getArtifactCoverageFile(root,"joaGridder3DAddin","joaGridder3DAddin");
		
		assertNotNull(coverageFile);
		assertTrue("coverage file of this project should exist",coverageFile.exists());
	}

}
