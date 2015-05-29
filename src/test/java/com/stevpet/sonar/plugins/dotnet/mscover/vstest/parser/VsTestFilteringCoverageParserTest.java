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
