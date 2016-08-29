package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.mockito.MockitoAnnotations;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.parser.ParserData;
import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestModuleNameObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestCoverageParserSubject;

public class VsTestModuleNameObserverTest {
	private VsTestModuleNameObserver observer;
	@Mock ParserData parserData;
    private XmlParserSubject parser;
    private File file;
	@Before
	
	public void before() {
		MockitoAnnotations.initMocks(this);
		observer = new VsTestModuleNameObserver();
		parser=new XmlParserSubject(parserData);

		parser.registerObserver(observer);
		file = TestUtils.getResource("observers/VsTestModuleNameObserver.xml");
		assertNotNull("file not found (mvn clean install)",file);
	}
	
	
	@Test
	public void noModuleSpecified_ShouldParseAllModules() {
	    parser.parseFile(file);
	    
		verify(parserData,times(0)).setSkipThisLevel();
	}
	
	@Test
	public void nullModuleSpecified_ShouldParseAllModules() {
		List<String> modules = null;
		observer.setModulesToParse(modules);
	    parser.parseFile(file);
	    
		verify(parserData,times(0)).setSkipThisLevel();	
	}
	
	@Test
	public void oneModuleSpecified_ShouldParseThatModule() {
		List<String> modules = new ArrayList<String>();
		modules.add("tfsblame.exe");
		observer.setModulesToParse(modules);
	    parser.parseFile(file);
	      
		verify(parserData,times(0)).setSkipThisLevel();		
	}
	
	@Test
	public void oneModuleSpecified_ShouldNotParseOtherModule() {
		List<String> modules = new ArrayList<String>();
		modules.add("donotparseme");
		observer.setModulesToParse(modules);
        parser.parseFile(file);
        
		verify(parserData,times(1)).setSkipThisLevel();		
	}
	
}
