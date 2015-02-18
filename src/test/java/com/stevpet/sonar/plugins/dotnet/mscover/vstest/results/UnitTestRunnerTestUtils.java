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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesStub;

public class UnitTestRunnerTestUtils {

    private UnitTestRunnerTestUtils() {
        
    }
    public static MsCoverProperties mockUnitTestRunnerSettingsToRun() {
        MsCoverPropertiesStub propertiesHelper = new MsCoverPropertiesStub();
        propertiesHelper.setMode("runvstest");
        propertiesHelper.setRunVsTest(true);
        propertiesHelper.setTestSettings("Testsettings1.testsettings");
        propertiesHelper.setUnitTestAssemblies("**/bin/Debug/*Test*");
        
        propertiesHelper.setUnitTestCoveragePath("coverage.xml");
        
        //when(settings.getString("sonar.mscover.mode")).thenReturn("runvstest");
        //when(settings.getString("sonar.mscover.coveragetool")).thenReturn("vstest");
        //when(settings.getString("sonar.mscover.vstest.testsettings")).thenReturn("Testsettings1.testsettings");
        //when(settings.getString("sonar.mscover.vstest.coverage2xml")).thenReturn("C:/Program Files (x86)/Baker Hughes/Coverage2Xml/CodeCoverage.exe"); 
        //when(settings.getString("sonar.mscover.unittests.assemblies")).thenReturn("**/bin/Debug/*Test*");
        //when(settings.getString("sonar.mscover.unittests.coveragexml")).thenReturn("coverage.xml");
        return propertiesHelper;
    }
}
