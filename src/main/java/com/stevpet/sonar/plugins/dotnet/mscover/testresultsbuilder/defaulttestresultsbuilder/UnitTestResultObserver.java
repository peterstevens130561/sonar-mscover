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
package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.time.LocalTime;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.parser.observer.TopLevelObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.ParserHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestingResults;

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

    private ParserHelper parserHelper = new ParserHelper();
    
    private UnitTestingResults registry;
    private UnitTestMethodResult unitTestResult;
    public void setRegistry(UnitTestingResults registry) {
        this.registry = registry;
    }
 
    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
        registrar.inPath("Results").inElement("UnitTestResult")
                .onAttribute("testId", this::testId)
                .onAttribute("testName", (value-> unitTestResult.setTestName(value)))
                .onAttribute("outcome",  (value ->unitTestResult.setOutcome(value)))
                .onAttribute("duration", this::duration)
                .onAttribute("relativeResultsDirectory",(value ->unitTestResult.setRelativeResultsDirectory(value))
                );
           
        registrar.inPath("Results/UnitTestResult/Output/ErrorInfo").onElement("Message",(value ->unitTestResult.setMessage(value)))
        .onElement("StackTrace", (value -> unitTestResult.setStackTrace(value)));
    }
    

    public void testId(String value) {
        unitTestResult=registry.getById(value);
        if(unitTestResult==null) {
            unitTestResult = registry.newEntry().setTestId(value).addToParent();    
        }
    }
  
    public void duration(String value) {
        LocalTime testDuration= parserHelper.parseDurationToTime(value);
        unitTestResult.setTime(testDuration);
    }
    
    

}
