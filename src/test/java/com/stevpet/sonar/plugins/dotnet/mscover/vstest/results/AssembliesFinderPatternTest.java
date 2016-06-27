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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.anyString;

public class AssembliesFinderPatternTest {
    private Settings settings;
    private MsCoverConfiguration propertiesHelper ;
    private AssembliesFinder finder;
    
    @Before()
    public void before() {
        settings=mock(Settings.class);
        when(settings.getStringArrayBySeparator(anyString(), anyString())).thenCallRealMethod();
        propertiesHelper = DefaultMsCoverConfiguration.create(settings);
        finder = new DefaultAssembliesFinder(propertiesHelper);
    }
    @Test
    public void simpleFinder() throws IOException {

        setPattern("**/bin/Debug/UnitTestProject");
        File root = TestUtils.getResource("Mileage");
        List<String> result=fromMSCoverProperty(root);
        Assert.assertEquals(1, result.size());
    }
    

    @Test
    public void simpleFinderWild() throws IOException {

        setPattern("**/bin/Debug/UnitTest*");
        File root = TestUtils.getResource("Mileage");
        List<String> result=fromMSCoverProperty(root);
        Assert.assertEquals(1, result.size());
    }
    
    @Test
    public void simpleFinderWilder() throws IOException {

        setPattern("**/bin/Debug/*Test*,**/bin/Debug/*Mileage*");
        File root = TestUtils.getResource("Mileage");
        List<String> result=fromMSCoverProperty(root);
        Assert.assertEquals(3, result.size());
    }
    
    @Test(expected=IllegalStateException.class)
    public void simpleFinderExplicit_Nothin() throws IOException {

        setPattern("unitTestProject");
        File root = TestUtils.getResource("Mileage");
        fromMSCoverProperty(root);
    }
    
    private void setPattern(String pattern) {
        when(settings.getString(eq(DefaultMsCoverConfiguration.MSCOVER_UNITTEST_ASSEMBLIES))).thenReturn(pattern);   
    }
    
    private List<String> fromMSCoverProperty(File root) {
        return finder.findUnitTestAssembliesFromConfig(root, null);
    }
}
