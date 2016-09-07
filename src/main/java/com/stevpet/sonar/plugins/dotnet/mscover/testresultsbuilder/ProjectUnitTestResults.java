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
package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;

/**
 * The collection of unit test results
 */
public class ProjectUnitTestResults {
    private Collection<ClassUnitTestResult> collection = new ArrayList<ClassUnitTestResult>();

    public void addAll(Collection<ClassUnitTestResult> collection) {
        this.collection.addAll(collection);
    }

    public Collection<ClassUnitTestResult> values() {
        return collection;
    }

    public ClassUnitTestResult addFile(File file) {
        ClassUnitTestResult classUnitTestResult = new ClassUnitTestResult(file);
        collection.add(classUnitTestResult);
        return classUnitTestResult;
    }

    public int getTests() {
    	int tests=0;
    	for(ClassUnitTestResult classUnitTestResult:collection) {
    		tests += classUnitTestResult.getTests();
    	}
    	return tests;
    }
    public void addFiles(String... names) {
        for (String name : names) {
            addFile(new File(name));
        }
    }

}
