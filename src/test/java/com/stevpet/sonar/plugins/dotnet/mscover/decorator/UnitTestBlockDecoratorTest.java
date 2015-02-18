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
package com.stevpet.sonar.plugins.dotnet.mscover.decorator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.TimeMachineMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.SettingsMock;

public class UnitTestBlockDecoratorTest {
    TimeMachineMock timeMachineMock = new TimeMachineMock();
    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    DecoratorContextMock decoratorContextMock = new DecoratorContextMock();
    SettingsMock settingsMock = new SettingsMock();
    private UnitTestBlockDecorator  decorator;
    @Before
    public void before() {
        msCoverPropertiesMock.givenUnitTestsEnabled(false);
        createUnitTestBlockDecorator();
    }
    
    @Test 
    public void createDecorator() {
        Assert.assertNotNull(decorator);
    }

    @Test
    public void shouldExecute_Set_ExpectTrue() {
        msCoverPropertiesMock.givenUnitTestsEnabled(true);
        boolean shouldExecute = whenInvokeShouldExecute();
        Assert.assertTrue("decorator shouldExecute",shouldExecute);
    }


    
    @Test
    public void shouldExecute_NotSet_ExpectFalse() {
        msCoverPropertiesMock.givenUnitTestsEnabled(false);
        boolean shouldExecute = whenInvokeShouldExecute();
        Assert.assertFalse("decorator shouldExecute",shouldExecute);
    }
    
    @Test 
    public void handleUncoveredResource_ShouldSaveMeasures() {
        decorator.handleUncoveredResource(decoratorContextMock.getMock(), 4.0);
        decoratorContextMock.verifySaveMeasureInvoked(3);
    }
    
    @Test
    public void generatesCoverageMetrics_ShouldHaveAll() {
        List<Metric> metrics = decorator.generatesCoverageMetrics();
        Assert.assertEquals(5,metrics.size());
    }
    
    private void createUnitTestBlockDecorator() {
        decorator = new UnitTestBlockDecorator(msCoverPropertiesMock.getMock(),timeMachineMock.getMock());
    }
    
    private boolean whenInvokeShouldExecute() {
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, msCoverPropertiesMock.getMock());
        return shouldExecute;
    }
}
