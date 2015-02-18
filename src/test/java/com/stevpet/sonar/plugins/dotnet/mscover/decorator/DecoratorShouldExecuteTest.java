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

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.batch.Decorator;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;

public class DecoratorShouldExecuteTest {

    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    @Test
    public void shouldExecute_PropertyNotSet_Disabled() {
        //Given
        msCoverPropertiesMock.givenIntegrationTestsEnabled(false);
     
        Decorator decorator = new CoverageDecoratorStub(msCoverPropertiesMock.getMock());
        //When
        boolean shouldExecute = decorator.shouldExecuteOnProject(null);
        //Then
        Assert.assertFalse(shouldExecute);
    }
    
    @Test
    public void shouldExecute_PropertySet_Enabled() {
        //Given
        msCoverPropertiesMock.givenIntegrationTestsEnabled(true);
        Decorator decorator = new CoverageDecoratorStub(msCoverPropertiesMock.getMock());
        //When
        boolean shouldExecute = decorator.shouldExecuteOnProject(null);
        //Then
        Assert.assertTrue(shouldExecute);
    }
    
    @Test 
    public void shouldExecuteIntegrationTests_PropertySet_Enabled() {
        //Given
        msCoverPropertiesMock.givenIntegrationTestsEnabled(true);
        Decorator decorate = new IntegrationTestLineDecorator(msCoverPropertiesMock.getMock(),null);
        //When
        boolean actual = decorate.shouldExecuteOnProject(null);
        //Then
        Assert.assertTrue(actual);
    }
    

 
}
