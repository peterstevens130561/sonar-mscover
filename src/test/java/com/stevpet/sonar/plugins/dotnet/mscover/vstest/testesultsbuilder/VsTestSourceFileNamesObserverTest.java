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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.VsTestObserverTest;

public class VsTestSourceFileNamesObserverTest extends VsTestObserverTest {
	private XmlParser parser;
	private SourceFileNameTable sourceFileNameTable;
	
	@Before
	public void before() {
		VsTestSourceFileNamesObserver observer = new VsTestSourceFileNamesObserver();
		parser = new DefaultXmlParser();
		parser.registerObserver(observer);
		sourceFileNameTable = new SourceFileNameTable();
		observer.setRegistry(sourceFileNameTable);
	}
	
	@Test
	public void emptyCoverage_NoElementsInTable() {
		createNewDoc();
		String doc = docToString();
		parser.parseString(doc);
		assertEquals("elements",0,sourceFileNameTable.size());
	}
	
	@Test
	public void oneFile_OneElementInTable() {
		createOneFile();
		assertEquals("elements",1,sourceFileNameTable.size());		
	}
	
	@Test
	public void twoFiles_TwoElementsInTable() {
		createNewDoc();
		createFileName("file1", "1");
		createFileName("file2", "10");
		String doc = docToString();
		parser.parseString(doc);
		assertEquals("elements",2,sourceFileNameTable.size());		
	}
	@Test
	public void oneFile_ShouldBeRightElement() {
		createOneFile();
		assertEquals("expect name","file1",sourceFileNameTable.getSourceFileName("1"));
	}

	private void createOneFile() {
		createNewDoc();
		createFileName("file1", "1");
		String doc = docToString();
		parser.parseString(doc);
	}
}
