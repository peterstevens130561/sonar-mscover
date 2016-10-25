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
import static org.mockito.Mockito.eq;

public class OpenCoverModuleSplitterTest {

	private CoverageHashes coverageHashes = new CoverageHashes();
	private String projectName = "bogus";
	private File root = new File("bogus");
    @Test
	public void simpleTest() throws FileNotFoundException, XMLStreamException, TransformerException {
		File testCoverageFile = TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
		CoverageModuleSaver coverageModuleSaver = mock(CoverageModuleSaver.class);
		new OpenCoverModuleSplitter(coverageModuleSaver,coverageHashes).splitCoverageFileInFilePerModule(root, projectName, testCoverageFile);
		verify(coverageModuleSaver,times(34)).save(eq(root),eq(projectName),anyString());
	}
	
	@Test
	public void fullTest() throws FileNotFoundException, XMLStreamException, TransformerException {
		File testCoverageFile = TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
		ModuleParser moduleParser = new OpenCoverModuleParser();
		Files.createTempDir();
		
		OpenCoverCoverageModuleSaver moduleLambda = new OpenCoverCoverageModuleSaver(moduleParser);
		new OpenCoverModuleSplitter(moduleLambda,coverageHashes).splitCoverageFileInFilePerModule(root, projectName, testCoverageFile);
	}

}