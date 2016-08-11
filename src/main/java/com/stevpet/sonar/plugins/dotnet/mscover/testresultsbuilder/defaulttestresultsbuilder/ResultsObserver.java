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


import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.common.parser.ObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.TestResults;

public class ResultsObserver extends BaseParserObserver {


    public ResultsObserver() {
        setPattern("ResultSummary/Counters");
    }
    
    private TestResults data;

    public void setRegistry(TestResults data) {
        this.data = data;
    }
    
   
    @AttributeMatcher(attributeName = "executed", elementName = "Counters")
    public void executedMatcher(String attributeValue) {
        data.setExecutedTests(Integer.parseInt(attributeValue));
    }
    
    @AttributeMatcher(attributeName="passed",elementName="Counters")
    public void passedMatcher(String attributeValue) {
        data.setPassedTests(Integer.parseInt(attributeValue));
    }

    @AttributeMatcher(attributeName="failed",elementName="Counters")
    public void failedMatcher(String attributeValue) {
        data.setFailedTests(Integer.parseInt(attributeValue));
    }
    
    @AttributeMatcher(attributeName="error",elementName="Counters")
    public void erroredMatcher(String attributeValue) {
        data.setErroredTests(Integer.parseInt(attributeValue));
    }


    @Override
    public void registerObservers(ObserverRegistrar registrar) {
        // TODO Auto-generated method stub
        
    }
}
