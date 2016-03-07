/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * holds the parsed unit testing results
 */
public class UnitTestingResults {
    private static Logger Log = LoggerFactory.getLogger(UnitTestingResults.class);
    // Key: MethodId
    private Map<String, UnitTestMethodResult> unitTestResultsById = new HashMap<String, UnitTestMethodResult>();

    void add(UnitTestMethodResult unitTestResult) {
        String testId = unitTestResult.getTestId();
        if (unitTestResultsById.containsKey(testId)) {
            Log.warn("UnitTestResult for test already stored :" + testId);
        }
        unitTestResultsById.put(testId, unitTestResult);
    }

    public UnitTestMethodResult newEntry() {
        return new UnitTestMethodResult(this);
    }

    /**
     * gets the testResult identified by the name
     * 
     * @param testName
     * @return
     */
    public UnitTestMethodResult getById(String testName) {
        return unitTestResultsById.get(testName);
    }

    public int size() {
        return unitTestResultsById.size();
    }

    public Collection<UnitTestMethodResult> values() {
        return unitTestResultsById.values();
    }
}
