/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
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
