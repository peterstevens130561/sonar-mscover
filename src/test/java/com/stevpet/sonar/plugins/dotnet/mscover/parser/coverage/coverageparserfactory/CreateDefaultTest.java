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
package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.coverageparserfactory;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.ConcreteVsTestParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestParserFactory;

public class CreateDefaultTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        //Arrange
        VsTestParserFactory factory = new ConcreteVsTestParserFactory();
        VsTestCoverageRegistry registry = new VsTestCoverageRegistry("C:\\users\\stevpet\\GitHub");
        List<String> modules = new ArrayList<String>();
        XmlParserSubject parser = factory.createCoverageParser(registry,modules);
        
        File file=TestUtils.getResource("mscoverage.xml");
        //Act
        parser.parseFile(file);
        //Assert
        assertEquals(8,registry.getSourceFileNameTable().values().size());
    }

}
