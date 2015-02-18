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
package com.stevpet.sonar.plugins.dotnet.mscover.datefilter;

import org.junit.Assert;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.TimeMachineMock;



public class DateFilterFactoryTest {
    private MsCoverPropertiesMock propertiesMock = new MsCoverPropertiesMock();
    private TimeMachineMock timeMachineMock = new TimeMachineMock();
    @Test
    public void createEmptyFilter_ShouldExist() {
        //Arrange
        //Act
        DateFilter emptyFilter = DateFilterFactory.createEmptyDateFilter();
        //Assert
        Assert.assertNotNull(emptyFilter);
        Assert.assertTrue(emptyFilter.isResourceIncludedInResults(null));
    }
    
    @Test 
    public void createCutOffDateFilterNoDate_AlwaysPassThrough() {
        //Arrange

        propertiesMock.givenCutOffDate(null);     
        //Act
        DateFilter filter = DateFilterFactory.createCutOffDateFilter(timeMachineMock.getMock(), propertiesMock.getMock());
        //Assert
        Assert.assertTrue(filter.getClass().equals(AlwaysPassThroughDateFilter.class));     
    }
    
    @Test 
    public void createCutOffDateFilterWithFilter_GetCutoff() {
        //Arrange
        propertiesMock.givenCutOffDate("2014-03-01");      
        //Act
        DateFilter filter = DateFilterFactory.createCutOffDateFilter(timeMachineMock.getMock(), propertiesMock.getMock());
        //Assert
        Assert.assertTrue(filter.getClass().equals(CutOffDateFilter.class));     
    }
}
