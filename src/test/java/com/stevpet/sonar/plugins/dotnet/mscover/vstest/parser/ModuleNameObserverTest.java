package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.mockito.MockitoAnnotations;

import com.stevpet.sonar.plugins.common.parser.ParserData;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestModuleNameObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestCoverageParserSubject;

public class ModuleNameObserverTest {
	private VsTestModuleNameObserver observer;
	@Mock ParserData parserData;
	@Before
	
	public void before() {
		MockitoAnnotations.initMocks(this);
		observer = new VsTestModuleNameObserver();
		new VsTestCoverageParserSubject();
		observer.setParserData(parserData);
		//parser.registerObserver(observer);
	}
	
	
	@Test
	public void noModuleSpecified_ShouldParseAllModules() {
		observer.moduleNameMatcher("bogus");
		verify(parserData,times(0)).setSkipThisLevel();
	}
	
	@Test
	public void nullModuleSpecified_ShouldParseAllModules() {
		List<String> modules = null;
		observer.setModulesToParse(modules);
		observer.moduleNameMatcher("parseme");
		verify(parserData,times(0)).setSkipThisLevel();	
	}
	
	@Test
	public void oneModuleSpecified_ShouldParseThatModule() {
		List<String> modules = new ArrayList<String>();
		modules.add("parseme");
		observer.setModulesToParse(modules);
		observer.moduleNameMatcher("parseme");
		verify(parserData,times(0)).setSkipThisLevel();		
	}
	
	@Test
	public void oneModuleSpecified_ShouldNotParseOtherModule() {
		List<String> modules = new ArrayList<String>();
		modules.add("parseme");
		observer.setModulesToParse(modules);
		observer.moduleNameMatcher("donotparseme");
		verify(parserData,times(1)).setSkipThisLevel();		
	}
	
	@Test
	public void twoModulesSpecified_ShouldParseThoseModules() {
		List<String> modules = new ArrayList<String>();
		modules.add("parseme");
		modules.add("parsemeto");
		observer.setModulesToParse(modules);
		observer.moduleNameMatcher("parseme");
		observer.moduleNameMatcher("parsemeto");
		verify(parserData,times(0)).setSkipThisLevel();			
	}
}
