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
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Scopes;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.ResourceMock;


public class DecoratorDecorateTest {
    private ResourceMock resourceMock = new ResourceMock();
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private DecoratorContextMock decoratorContextMock = new DecoratorContextMock();
    private CoverageDecoratorStub decorator;
    @Before
    public void before() {

        decorator = new CoverageDecoratorStub(msCoverPropertiesMock.getMock());
    }
    
    @Test
    public void Decorate_NotFile_NothingDone() {
        //Given
        resourceMock.givenScope(Scopes.DIRECTORY);
        decoratorContextMock.givenMeasure(CoreMetrics.NCLOC, null);   
        //When
        decorator.decorate(resourceMock.getMock(),null);
        //Then
        Assert.assertEquals(0, decorator.getCalls());
    }
    
    public void Decorate_EmptyFile_NothingDone() {
        //Given
        resourceMock.givenScope(Scopes.FILE);
        decoratorContextMock.givenMeasure(CoreMetrics.NCLOC, null);   
        //When
        decorator.decorate(resourceMock.getMock(),decoratorContextMock.getMock());
        //Then
        Assert.assertEquals(0, decorator.getCalls());
    }
}

