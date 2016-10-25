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

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.ParserData;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestModuleNameObserver;

public class VsTestModuleNameObserverTest {
	private VsTestModuleNameObserver observer;
	@Mock ParserData parserData;
    private XmlParser parser;
    private File file;
	@Before
	
	public void before() {
		MockitoAnnotations.initMocks(this);
		observer = new VsTestModuleNameObserver();
		parser=new DefaultXmlParser(parserData);

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
