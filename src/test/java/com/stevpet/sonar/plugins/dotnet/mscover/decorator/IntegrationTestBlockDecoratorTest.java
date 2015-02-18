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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesStub;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

public class IntegrationTestBlockDecoratorTest {
    TimeMachine timeMachine;
    Settings settings;
    MsCoverPropertiesStub propertiesHelper;
    DecoratorContext context;
    BaseDecorator decorator;
    @Before
    public void before() {
        timeMachine = mock(TimeMachine.class);
        propertiesHelper = new MsCoverPropertiesStub();
        context = mock(DecoratorContext.class);
        settings =mock(Settings.class);
    }
    
    @Test 
    public void CreateDecorator() {
        createDecorator();
        Assert.assertNotNull(decorator);
    }

    private void createDecorator() {
        decorator = new IntegrationTestBlockDecorator(propertiesHelper,timeMachine) ;
    }
    
    @Test
    public void ShouldExecute_Set_ExpectTrue() {
        propertiesHelper.setIntegrationTestsEnabled(true);
        createDecorator();
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, propertiesHelper);
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void ShouldExecute_NotSet_ExpectFalse() {
        when(settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH)).thenReturn(null);
        createDecorator();
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, propertiesHelper);
        Assert.assertFalse(shouldExecute);
    }
    
    @Test 
    public void handleUncoveredResource_ShouldSaveMeasures() {
        createDecorator();
        decorator.handleUncoveredResource(context, 4.0);
        verify(context,times(3)).saveMeasure(Matchers.any(Metric.class), Matchers.any(Double.class));
    }
    
    @Test
    public void generatesCoverageMetrics_ShouldHaveAll() {
        createDecorator(); 
        List<Metric> metrics = ((IntegrationTestBlockDecorator)decorator).generatesCoverageMetrics();
        Assert.assertEquals(5,metrics.size());
    }
}
