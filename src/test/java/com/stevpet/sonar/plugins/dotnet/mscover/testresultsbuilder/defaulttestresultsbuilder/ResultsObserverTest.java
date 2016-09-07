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
package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.TestResults;


public class ResultsObserverTest {
    private final ResultsObserver observer = new ResultsObserver();
    private TestResults data;
    
    @Before
    public void before() {
        XmlParser parser = new DefaultXmlParser();

        File xmlFile = TestUtils.getResource("observers/ResultsObserver.xml");
        data= new TestResults();
        parser.registerObserver(observer);
        observer.setRegistry(data);
        parser.parseFile(xmlFile);
    }
    
    @Test
    public void checkExecuted() {
        assertEquals("executed",120,data.getExecutedTests());
    }
    
    @Test
    public void checkPassed() {
        assertEquals("passed",100,data.getPassedTests());
    }
    
    @Test
    public void checkFailed() {
        assertEquals("failed",20,data.getFailedTests());
    }
    
    @Test
    public void checkErrored() {
        assertEquals("error",5,data.getErroredTests());
    }
}
