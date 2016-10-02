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
package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;

public class MethodToSourceFileIdMapTest {

	MethodToSourceFileIdRepository map = new MethodToSourceFileIdRepository();
	
	@Test
	public void emptyMapReturnsNull() {
		MethodId methodId = new MethodId("module.dll","namespace","Class","Method");
		String sourceFileId=map.getFileId(methodId);
		assertNull("map is empty",sourceFileId);	
	}
	
	@Test
	public void isInMap() {
		MethodId methodId = new MethodId("module.dll","namespace","Class","Method");
		String sourceFileId="10";
		map.add(sourceFileId, methodId);
		MethodId sameMethodId = new MethodId("module.dll","namespace","Class","Method");
		String returnedSourceFileId=map.getFileId(sameMethodId);
		assertEquals("should return the original one",sourceFileId,returnedSourceFileId);	
	}
	
	@Test
	public void isNotInMap() {
		MethodId methodId = new MethodId("module.dll","namespace","Class","Method");
		String sourceFileId="10";
		map.add(sourceFileId, methodId);
		MethodId sameMethodId = new MethodId("module.dll","namespace","Class","Method2");
		String returnedSourceFileId=map.getFileId(sameMethodId);
		assertNull("not in map, should return null",returnedSourceFileId);	
	}
	
	@Test
	public void isInherited() {
		String sourceFileId="10";
		map.add(sourceFileId, new MethodId("module.dll","namespace","Class","Method"));
		
		String returnedSourceFileId=map.getFileId(new MethodId("module.dll","namespace","OtherClass","Method"));
		
		assertEquals("though in different classshould return the original one",sourceFileId,returnedSourceFileId);
	}
	
	@Test
	public void isAmbiguous() {
		MethodId methodId = new MethodId("module.dll","namespace","Class","Method");
		map.add("10", methodId);
		
		methodId = new MethodId("module.dll","namespace","OtherClass","Method");
		map.add("11", methodId);
		
		MethodId sameMethodId = new MethodId("module.dll","namespace","FakeClass","Method");
		String returnedSourceFileId=map.getFileId(sameMethodId);
		assertEquals("the fake class will fallback to the last one added","11",returnedSourceFileId);
	}
}
