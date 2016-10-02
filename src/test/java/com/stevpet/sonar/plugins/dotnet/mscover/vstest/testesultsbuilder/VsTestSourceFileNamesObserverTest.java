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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl.DefaultSourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.VsTestObserverTest;

public class VsTestSourceFileNamesObserverTest extends VsTestObserverTest {
	private XmlParser parser;
	private DefaultSourceFileRepository sourceFileNameTable;
	
	@Before
	public void before() {
		VsTestSourceFileNamesObserver observer = new VsTestSourceFileNamesObserver();
		parser = new DefaultXmlParser();
		parser.registerObserver(observer);
		sourceFileNameTable = new DefaultSourceFileRepository();
		observer.setRegistry(sourceFileNameTable);
	}
	
	@Test
	public void emptyCoverage_NoElementsInTable() {
		createNewDoc();
		String doc = docToString();
		parser.parseString(doc);
		assertEquals("elements",0,sourceFileNameTable.stream().count());
	}
	
	@Test
	public void oneFile_OneElementInTable() {
		createOneFile();
		assertEquals("elements",1,sourceFileNameTable.stream().count());		
	}
	
	@Test
	public void twoFiles_TwoElementsInTable() {
		createNewDoc();
		createFileName("file1", "1");
		createFileName("file2", "10");
		String doc = docToString();
		parser.parseString(doc);
		assertEquals("elements",2,sourceFileNameTable.stream().count());		
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
