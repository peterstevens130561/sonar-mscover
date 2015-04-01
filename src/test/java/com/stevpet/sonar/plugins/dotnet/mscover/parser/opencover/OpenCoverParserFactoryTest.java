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
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.ConcreteOpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.ParserObserver;

public class OpenCoverParserFactoryTest {
    @Test
    public void createOpenCoverParser_ShouldSimplyParse() {
        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        SonarCoverage registry = new SonarCoverage();
        XmlParserSubject parser = parserFactory.createOpenCoverParser(registry);
        File file = TestUtils.getResource("coverage-report.xml");
        parser.parseFile(file);
        OpenCoverObserversTest.checkCoverageParsingResults(registry);
    }
       
    @Test
    public void createOpenCoverParserWithoutMissingPdb_ShouldHaveNormalObservers() {
        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        SonarCoverage registry = new SonarCoverage();
        XmlParserSubject parser = parserFactory.createOpenCoverParser(registry);
        List<ParserObserver> observers=parser.getObservers();
        assertEquals(3,observers.size());
        boolean found=false;
        for(ParserObserver observer : observers) {
            if ( observer instanceof OpenCoverMissingPdbObserver ) {
                found=true;
            }
        }
        assertTrue(found);
    }
    
    @Test
    public void createOpenCoverParserWithMissingPdb_ShouldHaveNewObservers() {
        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        SonarCoverage registry = new SonarCoverage();
        List<String> missingPdbsThatCanBeIgnored = new ArrayList<String>();
        MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
        msCoverPropertiesMock.givenUnitTestAssembliesThatCanBeIgnoredIfMissing(missingPdbsThatCanBeIgnored);
        XmlParserSubject parser = parserFactory.createOpenCoverParser(registry,msCoverPropertiesMock.getMock());
        List<ParserObserver> observers=parser.getObservers();
        assertEquals(3,observers.size());
        boolean foundOrig=checkFound(observers,OpenCoverMissingPdbObserver.class);
        boolean foundNew=checkFound(observers,OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs.class);
        assertFalse(foundOrig);
        assertTrue(foundNew);
    }
    
    
    public boolean checkFound(List<ParserObserver> observers,@SuppressWarnings("rawtypes") Class clazz) {
        boolean found=false;
        for(ParserObserver observer : observers) {
            if ( observer.getClass()== clazz ) {
                found=true;
            }
        }  
        return found;
    }
 
    
}
