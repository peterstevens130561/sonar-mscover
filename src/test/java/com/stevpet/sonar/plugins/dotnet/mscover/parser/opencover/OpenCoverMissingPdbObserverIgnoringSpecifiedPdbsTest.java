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
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.ParserSubjectErrorException;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs;

public class OpenCoverMissingPdbObserverIgnoringSpecifiedPdbsTest {
    private OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs observer;
    private List<String> pdbs = new ArrayList<String>();
    @Test(expected=ParserSubjectErrorException.class)
    public void reportWithTwoMissingPdbs_ExpectExepction() {
        //Arrange
        XmlParser parser = initializeParser();
        File file = TestUtils.getResource("coverage-report-missingPdbs.xml");
        //Act
        parser.parseFile(file);
    }
  
    @Test(expected=ParserSubjectErrorException.class)
    public void reportWithTwoMissingPdbs_IgnoreBothExpectException() {
        //Arrange
        XmlParser parser = initializeParser();
        pdbs.add("joaJewelUtilitiesUI.dll");
        File file = TestUtils.getResource("coverage-report-missingPdbs.xml");
        //Act
        parser.parseFile(file);
    }
   
    @Test
    public void reportWithTwoMissingPdbs_IgnoreBothExpectNoException() {
        //Arrange
        XmlParser parser = initializeParser();
        pdbs.add("joaJewelUtilitiesUI.dll");
        pdbs.add("joaTouchEngine.dll");
        File file = TestUtils.getResource("coverage-report-missingPdbs.xml");
        //Act
        parser.parseFile(file);
    }
    @Test
    public void reportWithNoPdbsMissing_ExpectNone(){
        //Arrange
        XmlParser parser = initializeParser();
        File file = TestUtils.getResource("coverage-report.xml");
        //Act
        parser.parseFile(file);
        //no exception expected
    }
    
    private XmlParser initializeParser() {
        XmlParser parser = new DefaultXmlParser();
        observer = new OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs();
        observer.setPdbsThatCanBeIgnoredIfMissing(pdbs);
        observer.setRegistry(null);
        parser.registerObserver(observer);
        return parser;
    }

}
