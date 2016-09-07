/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.FilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestFilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class VsTestFilteringCoverageParserTest {

	private FilteringCoverageParser parser;
	private File coverageFile;
	private SonarCoverage sonarCoverage;

	@Before
	public void before() {
		sonarCoverage = new SonarCoverage();
		coverageFile = TestUtils.getResource("mscoverage.xml");	
		parser = new VsTestFilteringCoverageParser();		
	}
	
	@Test
	public void sampleFile_NoFiltering_ExpectSourceFiles() {
		//equal to not specifying any module
		parser.setModulesToParse(null);
		
		parser.parse(sonarCoverage, coverageFile);
		
		assertEquals("expect all 8 sourcefiles in assembly to be included",8,sonarCoverage.getValues().size());
	}
	
	@Test
	public void sampleFile_FilterApplied_ExpectSourceFiles() {
		//specify modules to parse
		List<String> modules = new ArrayList<String>();
		modules.add("tfsblame.exe");
		parser.setModulesToParse(modules);
		
		parser.parse(sonarCoverage, coverageFile);
		
		assertEquals("expect all 8 sourcefiles in assembly to be included",8,sonarCoverage.getValues().size());		
	}
	
	@Test
	public void sampleFile_FilterAppliedOnAssemblyNotInCoverage_ExpectNoSourceFiles() {
		//specify modules to parse
		List<String> modules = new ArrayList<String>();
		modules.add("bogus.exe");
		parser.setModulesToParse(modules);
		
		parser.parse(sonarCoverage, coverageFile);	
		
		assertEquals("expect no sourcefiles in assembly to be included",0,sonarCoverage.getValues().size());		
	}
}
