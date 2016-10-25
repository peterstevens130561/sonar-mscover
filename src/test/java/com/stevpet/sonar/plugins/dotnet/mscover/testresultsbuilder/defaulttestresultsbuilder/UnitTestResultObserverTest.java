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
package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.VsTestResults;

public class UnitTestResultObserverTest {

    private UnitTestResultObserver observer = new UnitTestResultObserver();
    
    @Test
    public void someMilliSeconds() {
        VsTestResults unitTestingResults = new VsTestResults();
        observer.setRegistry(unitTestingResults);
        observer.testId("1");
        observer.duration("00:00:59.12346795");
        UnitTestMethodResult result = unitTestingResults.getById("1");
        assertEquals("59123.467",result.getFormattedDuration());
    }
    
    @Test
    public void oneMilliSeconds() {
        VsTestResults unitTestingResults = new VsTestResults();
        observer.setRegistry(unitTestingResults);
        observer.testId("1");
        observer.duration("00:00:00.001230");
        UnitTestMethodResult result = unitTestingResults.getById("1");
        assertEquals("1.230",result.getFormattedDuration());
    }
    
    @Test
    public void almostoneday() {
        VsTestResults unitTestingResults = new VsTestResults();
        observer.setRegistry(unitTestingResults);
        observer.testId("1");
        observer.duration("23:59:59.99999");
        UnitTestMethodResult result = unitTestingResults.getById("1");
        assertEquals("86399999.989",result.getFormattedDuration());
    }
}
