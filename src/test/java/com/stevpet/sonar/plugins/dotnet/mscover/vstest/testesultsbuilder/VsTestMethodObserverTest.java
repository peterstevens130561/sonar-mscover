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
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.VsTestObserverTest;


public class VsTestMethodObserverTest extends VsTestObserverTest {
	private static final String CLASS_NAME = "class";
	private static final String NAMESPACE_NAME = "namespace";
	private static final String MODULE_NAME = "module.dll";
	private static final String FILEID = "8";
	private VsTestMethodObserver observer;
	private XmlParser parser;
	private MethodToSourceFileIdMap methodToSourceFileIdMap;
	@Before
	public void before() {
		observer = new VsTestMethodObserver();
		methodToSourceFileIdMap = new MethodToSourceFileIdMap();
		observer.setRegistry(methodToSourceFileIdMap);
		parser = new DefaultXmlParser();
		parser.registerObserver(observer);		
	}
	
	@Test
	public void emptyCoverageFile_EmptyMap() {
		this.createNewDoc();
		parser.parseString(docToString());
		assertEquals("expect empty map",0,methodToSourceFileIdMap.size());
	}
	
	@Test
	public void oneSimpleMethod_InMap() {
		this.createNewDoc();
		createModule(MODULE_NAME);
		createNamespaceTable(NAMESPACE_NAME);
		createClass(CLASS_NAME);
		createMethod("method(int32,string)");
		addLine(FILEID,"37","1");
		String doc=docToString();
		parser.parseString(doc);
		assertEquals("element in map",1,methodToSourceFileIdMap.size());
	}
	
	@Test
	public void oneSimpleMethod_CheckMapping() {
		this.createNewDoc();
		createModule(MODULE_NAME);
		createNamespaceTable(NAMESPACE_NAME);
		createClass(CLASS_NAME);
		createMethod("method(int32,string)");
		addLine(FILEID,"37","1");
		String doc=docToString();
		parser.parseString(doc);
		MethodId expectedId=new MethodId(MODULE_NAME,NAMESPACE_NAME,CLASS_NAME,"method");
		String actualFileId=methodToSourceFileIdMap.get(expectedId);
		assertEquals("fileId of method",FILEID,actualFileId);
	}
	
	@Test
	public void oneSimpleMethodTwoLines_OneMethod() {
		this.createNewDoc();
		createModule(MODULE_NAME);
		createNamespaceTable(NAMESPACE_NAME);
		createClass(CLASS_NAME);
		createMethod("method(int32,string)");
		addLine(FILEID,"37","1");
		addLine(FILEID,"38","1");
		String doc=docToString();
		parser.parseString(doc);
		assertEquals("element in map",1,methodToSourceFileIdMap.size());
	}
}
