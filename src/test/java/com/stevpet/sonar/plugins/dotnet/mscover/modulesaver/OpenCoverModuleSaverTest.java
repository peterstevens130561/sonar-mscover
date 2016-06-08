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
import org.junit.Test;
import org.sonar.test.TestUtils;


public class OpenCoverModuleSaverTest {

	@Test
	public void basicTest() throws XMLStreamException, TransformerException, IOException {
		File xmlFile = TestUtils.getResource("OpenCoverModulesSplitter/twomodules.xml");
		OpenCoverModuleSaver moduleSaver= new OpenCoverModuleSaver();
		Path rootPath = Files.createTempDirectory("cover");
		File root = rootPath.toFile();
		moduleSaver.setRoot(root).setProject("ProjectName").splitFile(xmlFile);
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
		OpenCoverModuleSaver moduleSaver= new OpenCoverModuleSaver();
		Path rootPath = Files.createTempDirectory("cover");
		File root = rootPath.toFile();
		moduleSaver.setRoot(root).setProject("joaGridder3DAddin").splitFile(xmlFile);
		File coverageFile=moduleSaver.getCoverageFile("joaGridder3DAddin");
		
		assertNotNull(coverageFile);
		assertTrue("coverage file of this project should exist",coverageFile.exists());
	}

}
