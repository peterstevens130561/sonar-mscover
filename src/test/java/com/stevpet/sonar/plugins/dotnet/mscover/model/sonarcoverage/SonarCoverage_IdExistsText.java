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
package com.stevpet.sonar.plugins.dotnet.mscover.model.sonarcoverage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class SonarCoverage_IdExistsText {

	private ProjectCoverageRepository sonarCoverage ;
	
	@Before()
	public void before() {
		sonarCoverage = new DefaultProjectCoverageRepository();
	}
	
	@Test
	public void emptyList_shouldNotExist() {
		boolean exist=sonarCoverage.fileIdExists("0");
		assertFalse("should not exist",exist);
	}
	
	@Test
	public void oneItem_shouldExist() {
		sonarCoverage.getCoverageOfFile("1234");
		boolean exist=sonarCoverage.fileIdExists("1234");
		assertTrue("inserted, requested same so should exist",exist);
	}
	
	@Test
	public void oneItem_findOthershouldNotExist() {
		sonarCoverage.getCoverageOfFile("1234");
		boolean exist=sonarCoverage.fileIdExists("1238");
		assertFalse("inserted, requested other so should not exist",exist);
	}
}
