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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.codehaus.plexus.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;

public class CoverageParserTest extends VsTestObserverTest {

	private ProjectCoverageRepository sonarCoverage;
	private CoverageParser coverageParser ;
	private File coverageFile;
	
	@Before
	public void before() {
		coverageParser = new VsTestCoverageParser();
		sonarCoverage = new DefaultProjectCoverageRepository();
		String tmpPath=System.getenv("TMP");
		File parentDir=new File(tmpPath);
		coverageFile=FileUtils.createTempFile("coverage", ".xml", parentDir);
	}
	
	@After
	public void after() {
		FileUtils.fileDelete(coverageFile.getAbsolutePath());
	}
	
	@Test
	public void emptyCoverageFile_EmptyCoverage() throws TransformerConfigurationException, IOException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {
		createCoverage();
		FileUtils.fileAppend(coverageFile.getAbsolutePath(), docToString());
		coverageParser.parse(sonarCoverage, coverageFile);
		// should be empty
		assertEquals("no coverage info expected",0,sonarCoverage.getValues().size());
	}
	
	
	
	@Test
	public void OneLiner_ExpectInCoverage() throws TransformerConfigurationException, IOException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {
		createOneFileCoverage();
		FileUtils.fileAppend(coverageFile.getAbsolutePath(), docToString());
		coverageParser.parse(sonarCoverage, coverageFile);
		// should be empty
		assertEquals("one file expected",1,sonarCoverage.getValues().size());
		SonarFileCoverage fileCoverage=sonarCoverage.getCoverageOfFile("1");
		assertEquals("filename","file1",fileCoverage.getAbsolutePath());
		CoverageLinePoint linePoint=fileCoverage.getLinePoints().getPoints().get(0);
		assertEquals("line",20,linePoint.getLine());
		
	}
	private void createCoverage() {
		createNewDoc();
	}
	
	private void createOneFileCoverage()  {
		createNewDoc();

		createModuleToMethod();
		addLine("1","20","0");
		createFileName("file1","1");

		
	}
}
