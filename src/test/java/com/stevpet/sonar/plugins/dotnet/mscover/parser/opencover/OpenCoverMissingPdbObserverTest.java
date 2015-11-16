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

import java.io.File;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.parser.ParserSubjectErrorException;
import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverMissingPdbObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverParserSubject;


public class OpenCoverMissingPdbObserverTest {

    private OpenCoverMissingPdbObserver observer;
    @Test(expected=ParserSubjectErrorException.class)
    public void reportWithTwoMissingPdbs_ExpectExepction() {
        //Arrange
        XmlParserSubject parser = initializeParser();
        File file = TestUtils.getResource("coverage-report-missingPdbs.xml");
        //Act
        parser.parseFile(file);
    }
    
    @Test
    public void reportWithNoPdbsMissing_ExpectNone(){
        //Arrange
        XmlParserSubject parser = initializeParser();
        File file = TestUtils.getResource("coverage-report.xml");
        //Act
        parser.parseFile(file);
        //no exception expected
    }
    
    private XmlParserSubject initializeParser() {
        XmlParserSubject parser = new OpenCoverParserSubject();
        observer = new OpenCoverMissingPdbObserver();
        observer.setRegistry(null);
        parser.registerObserver(observer);
        return parser;
    }

}
