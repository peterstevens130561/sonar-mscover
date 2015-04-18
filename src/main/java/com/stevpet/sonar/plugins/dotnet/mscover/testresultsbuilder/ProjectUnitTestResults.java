package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;

import java.util.ArrayList;
import java.util.Collection;


import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;

/**
 * The collection of unit test results

 */
public class ProjectUnitTestResults  {
	private Collection<ClassUnitTestResult> collection = new ArrayList<ClassUnitTestResult>();
		
	
	public void addAll(Collection<ClassUnitTestResult> collection) {
		this.collection.addAll(collection);
	}

	public Collection<ClassUnitTestResult> values() {
		return collection;
	}

}
