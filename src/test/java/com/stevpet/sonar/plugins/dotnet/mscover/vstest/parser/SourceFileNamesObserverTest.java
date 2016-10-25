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

import static org.junit.Assert.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class SourceFileNamesObserverTest extends VsTestObserverTest {
	private VsTestCoverageObserver observer;
	private ProjectCoverageRepository registry;
	private XmlParser parser;

	@Before
	public void before() {
		observer = new VsTestFileNamesObserver();
		registry = new DefaultProjectCoverageRepository();
		observer.setVsTestRegistry(registry);
		parser = new DefaultXmlParser();
		parser.registerObserver(observer);
	}
	
	@Test
	public void noFileNames() {
		createNewDoc();
		String coverageDoc=docToString();
		parser.parseString(coverageDoc);
		assertNotNull(registry.getValues());
		assertEquals("no elements expected",0,registry.getValues().size());
	}
	@Test
	public void oneFileName() throws ParserConfigurationException, TransformerException {
		createNewDoc();
		createFileName("first","1");
		String coverageDoc=docToString();
		//filenames will only be registered if the lines have been loaded
		registry.getCoverageOfFile("1");
		parser.parseString(coverageDoc);
		assertNotNull(registry.getValues());
		assertEquals("one element expected",1,registry.getValues().size());	
		assertEquals("expect file","first",registry.getCoverageOfFile("1").getAbsolutePath());
	}
	
	@Test
	public void twoFileNames() throws ParserConfigurationException, TransformerException {
		createNewDoc();
		createFileName("first","1");
		createFileName("second","10");
		String coverageDoc=docToString();
		// filenames will only be registered if the lines have been loaded
		registry.getCoverageOfFile("1");
		registry.getCoverageOfFile("10");
		parser.parseString(coverageDoc);
		assertNotNull(registry.getValues());
		assertEquals("two elements expected",2,registry.getValues().size());
		assertEquals("expect file","first",registry.getCoverageOfFile("1").getAbsolutePath());
		assertEquals("expect file","second",registry.getCoverageOfFile("10").getAbsolutePath());
	}
	
}

