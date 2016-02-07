package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;

public class MethodToSourceFileIdMapTest {

	MethodToSourceFileIdMap map = new MethodToSourceFileIdMap();
	
	@Test
	public void emptyMapReturnsNull() {
		MethodId methodId = new MethodId("module.dll","namespace","Class","Method");
		String sourceFileId=map.get(methodId);
		assertNull("map is empty",sourceFileId);	
	}
	
	@Test
	public void isInMap() {
		MethodId methodId = new MethodId("module.dll","namespace","Class","Method");
		String sourceFileId="10";
		map.add(methodId, sourceFileId);
		MethodId sameMethodId = new MethodId("module.dll","namespace","Class","Method");
		String returnedSourceFileId=map.get(sameMethodId);
		assertEquals("should return the original one",sourceFileId,returnedSourceFileId);	
	}
	
	@Test
	public void isNotInMap() {
		MethodId methodId = new MethodId("module.dll","namespace","Class","Method");
		String sourceFileId="10";
		map.add(methodId, sourceFileId);
		MethodId sameMethodId = new MethodId("module.dll","namespace","Class","Method2");
		String returnedSourceFileId=map.get(sameMethodId);
		assertNull("not in map, should return null",returnedSourceFileId);	
	}
	
	@Test
	public void isInherited() {
		String sourceFileId="10";
		map.add(new MethodId("module.dll","namespace","Class","Method"), sourceFileId);
		
		String returnedSourceFileId=map.get(new MethodId("module.dll","namespace","OtherClass","Method"));
		
		assertEquals("though in different classshould return the original one",sourceFileId,returnedSourceFileId);
	}
	
	@Test
	public void isAmbiguous() {
		MethodId methodId = new MethodId("module.dll","namespace","Class","Method");
		map.add(methodId, "10");
		
		methodId = new MethodId("module.dll","namespace","OtherClass","Method");
		map.add(methodId, "11");
		
		MethodId sameMethodId = new MethodId("module.dll","namespace","FakeClass","Method");
		String returnedSourceFileId=map.get(sameMethodId);
		assertEquals("the fake class will fallback to the last one added","11",returnedSourceFileId);
	}
}
