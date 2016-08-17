/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 SonarSource
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.model.TestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestingResults;

public class UnitTestDefinitionObserverTest {
    private final UnitTestObserver observer = new UnitTestObserver();
    private TestResults data;
    private UnitTestingResults registry = new UnitTestingResults();
    private UnitTestMethodResult result;
    
    @Before
    public void before() {
        XmlParserSubject parser = new VsTestResultsParserSubject();

        File xmlFile = TestUtils.getResource("observers/UnitTestDefinitionsObserver.xml");
        data= new TestResults();
        parser.registerObserver(observer);
        observer.setRegistry(registry);
        parser.parseFile(xmlFile);
        result=registry.getById("6dd2b593-aa2d-3a7b-1a71-f15debf90d70");
    }
    
    @Test
    public void getId() {
        assertNotNull("test stored",result);
    }
    
    @Test
    public void checkCodeBase() {
        assertEquals("codebase","c:\\Development\\Jewel.Release.Oahu.StructMod\\JewelEarth\\Core\\joaGeometries\\joaGeometriesUnitTest\\Debug\\joaGeometriesUnitTests.dll",result.getCodeBase());
    }
    
    @Test
    public void checModuleName() {
        assertEquals("module","joaGeometriesUnitTests.dll",result.getModuleName());
    }
    
    @Test
    public void checkClassName() {
        assertEquals("className","joaGeometriesUnitTests",result.getClassName());
    }
    
    @Test
    public void checkTestName() {
        assertEquals("testname","TestEqualSurfaces",result.getTestName());
    }
}
