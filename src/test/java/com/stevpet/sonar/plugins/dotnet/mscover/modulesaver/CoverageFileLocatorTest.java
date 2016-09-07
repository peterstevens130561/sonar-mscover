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
package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class CoverageFileLocatorTest {

	@Mock private ModuleParser parser;
	CoverageFileLocator coverageFileLocator ;
	
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		coverageFileLocator = new DefaultCoverageFileLocator();
	}
	
	@Test
	public void testOneDot() {
		File coverageFile=coverageFileLocator.getFile(new File("c:/root"),"projectName","hi.dll");
		assertEquals(new File("c:/root/hi/projectName.xml"),coverageFile);
	}
	
	@Test
	public void testTwoDots() {
		File coverageFile=coverageFileLocator.getFile(new File("c:/root"),"projectName","hi.john.dll");
		assertEquals(new File("c:/root/hi.john/projectName.xml"),coverageFile);
	}
	
	@Test
	public void testNoDots() {
		File coverageFile=coverageFileLocator.getFile(new File("c:/root"),"projectName","assemblyWithNoDots");
		assertEquals(new File("c:/root/assemblyWithNoDots/projectName.xml"),coverageFile);
	}
}
