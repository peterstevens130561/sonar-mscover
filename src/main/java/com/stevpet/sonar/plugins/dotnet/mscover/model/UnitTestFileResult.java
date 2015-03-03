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

import java.util.ArrayList;
import java.util.List;

public class UnitTestFileResult {
    private int error ;
    
    private List<UnitTestMethodResult> tests = new ArrayList<UnitTestMethodResult>() ;
    
    public void add(UnitTestMethodResult unitTest) {
        tests.add(unitTest);
        String outcome=unitTest.getOutcome();
        if(!"Passed".equals(outcome)) {
            error++;
        }
    }
    
    public double getPassed() {
        return (double) tests.size() - error;
    }
    
    public double getFail() {
        return (double) error ;
    }
    
    public double getTests() {
        return (double) tests.size();
    }
    public double getDensity() {
        if(tests.isEmpty()) {
            return 1.0;
        }
        return getPassed()/getTests();
    }
    
    public List<UnitTestMethodResult> getUnitTests() {
        return tests;
    }
}
