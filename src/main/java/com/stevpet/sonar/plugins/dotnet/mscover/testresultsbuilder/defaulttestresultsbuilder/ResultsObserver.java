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
import com.stevpet.sonar.plugins.common.parser.ObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.TestResults;

public class ResultsObserver extends BaseParserObserver {
    private TestResults data;

    public void setRegistry(TestResults data) {
        this.data = data;
    }
    
    @Override
    public void registerObservers(ObserverRegistrar registrar) {
        registrar.inPath("ResultSummary/Counters",counters -> counters
                .onAttribute("executed", (value ->data.setExecutedTests(Integer.parseInt(value))))
                .onAttribute("passed",(value ->data.setPassedTests(Integer.parseInt(value))))
                .onAttribute("failed",(value ->data.setFailedTests(Integer.parseInt(value))))
                .onAttribute("error",(value ->data.setErroredTests(Integer.parseInt(value))))
                );
     
    }

    
}
