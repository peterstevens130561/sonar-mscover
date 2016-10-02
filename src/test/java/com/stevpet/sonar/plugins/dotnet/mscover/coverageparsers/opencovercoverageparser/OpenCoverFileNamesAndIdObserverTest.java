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
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFile;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.SourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl.DefaultSourceFileRepository;

public class OpenCoverFileNamesAndIdObserverTest {

	private final OpenCoverFileNamesAndIdObserver observer = new OpenCoverFileNamesAndIdObserver();
	private DefaultSourceFileRepository registry = new DefaultSourceFileRepository();
	
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
		Optional<SourceFile> row = registry.stream().filter(r ->r.getId().equals("84")).findFirst();
		assertTrue(row.isPresent());
		assertEquals("c:\\Development\\bogus.cs",row.get().getPath());
	}
}
