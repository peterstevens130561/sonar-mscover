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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestingResults;

/*
 *   <UnitTestResult executionId="a3a9f8e0-1cbf-41c5-ae89-a579d2e6ece5" 
 *   testId="865b2842-73dd-b3d0-17a4-284b86e8ce98" 
 *   testName="TestBoxNotIntersects" 
 *   computerName="RDSJ741TY1" 
 *   duration="00:00:00.0006819" 
 *   startTime="2014-04-10T20:59:56.5584722+02:00" 
 *   endTime="2014-04-10T20:59:56.5594723+02:00" 
 *   testType="13cdc9d9-ddb5-4fa4-a97d-d965ccfc6d4b" 
 *   outcome="Passed" 
 *   testListId="8c84fa94-04c1-424b-9868-57a2d4851a1d" 
 *   relativeResultsDirectory="a3a9f8e0-1cbf-41c5-ae89-a579d2e6ece5" />
 */
public class UnitTestResultObserver extends BaseParserObserver {

    public UnitTestResultObserver() {
        setPattern("(Results/UnitTestResult)|(.*/Message)|(.*/StackTrace)");
    }
    
    private UnitTestingResults registry;
    private UnitTestMethodResult unitTestResult;
    public void setRegistry(UnitTestingResults registry) {
        this.registry = registry;
    }
 
    @AttributeMatcher(attributeName = "testId", elementName = "UnitTestResult")
    public void testId(String value) {
        unitTestResult=registry.getById(value);
        if(unitTestResult==null) {
            unitTestResult = new UnitTestMethodResult();
            unitTestResult.setTestId(value);
            registry.add(unitTestResult);      
        }
    }
    @AttributeMatcher(attributeName = "testName", elementName = "UnitTestResult")
    public void testName(String value) {
        unitTestResult.setTestName(value);
    }
 
    @AttributeMatcher(attributeName = "outcome", elementName = "UnitTestResult") 
    public void outcome(String value) {
        unitTestResult.setOutcome(value);
    }
    
    @AttributeMatcher(attributeName= "duration", elementName = "UnitTestResult")
    public void duration(String value) {
        unitTestResult.setDuration(value);
    }
    
    @AttributeMatcher(attributeName= "relativeResultsDirectory",elementName = "UnitTestResult")
    public void relativeResultsDirectory(String value) {
        unitTestResult.setRelativeResultsDirectory(value);
        
    }
    
    @ElementMatcher(elementName="Message")
    public void message(String value) {
        unitTestResult.setMessage(value);
    }
    
    @ElementMatcher(elementName="StackTrace")
    public void stackTrace(String value) {
        unitTestResult.setStackTrace(value);
    }
}
