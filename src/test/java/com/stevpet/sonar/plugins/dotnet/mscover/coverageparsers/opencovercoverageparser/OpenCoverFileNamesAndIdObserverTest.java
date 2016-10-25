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
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;

public class OpenCoverFileNamesAndIdObserverTest {

	private final OpenCoverFileNamesAndIdObserver observer = new OpenCoverFileNamesAndIdObserver();
	private SourceFileNameTable registry = new SourceFileNameTable();
	
	@Before
	public void before() {
		XmlParser parser = new DefaultXmlParser();
		parser.registerObserver(observer);
		observer.setRegistry(registry);
		File xmlFile = TestUtils.getResource("observers/OpenCoverFileNamesAndIdObserver.xml");
		parser.parseFile(xmlFile);
	}
	
	@Test
	public void checkFileId() {
		assertEquals("FileId",84,observer.getUid());	
	}
	
	@Test
	public void checkFileName() {
		assertEquals("FileName","c:\\Development\\bogus.cs",observer.getFileName());
	}
	
	@Test
	public void checkRegistry() {
		SourceFileNameRow row = registry.get(84);
		assertNotNull(row);
		assertEquals("c:\\Development\\bogus.cs",row.getSourceFileName());
		assertEquals(84,row.getSourceFileID());
	}
}
