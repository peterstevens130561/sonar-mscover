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
package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results.resultssensor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesStub;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestUnitTestResultsSensor;
public class ShouldExecuteTest {
    
    private MsCoverPropertiesStub propertiesHelper;
    private Project project ;
    private VsTestUnitTestResultsSensor resultsSensor;
    
    @Before
    public void before() {
        propertiesHelper = new MsCoverPropertiesStub();
        project = mock(Project.class);
        resultsSensor = new VsTestUnitTestResultsSensor(propertiesHelper, null, null,null,null);
    }
    
    @Test
    public void RunModeIsSkipNotExecuteRootNotProjectIsRootPathEmpty_False() {
        testSkipModeAlwaysFalse(false,false,StringUtils.EMPTY);
  
    }
    @Test
    public void RunModeIsSkipExecuteRootProjectIsRootPathEmpty_False() {
        testSkipModeAlwaysFalse(true,true,StringUtils.EMPTY);
  
    }
     
    @Test
    public void RunModeIsSkipExecuteRootProjectIsRootPathOk_False() {
        testSkipModeAlwaysFalse(true,true,"a/b");
  
    }
    
    @Test
    public void RunModeIsSkipExecuteRootProjectIsNotRootPath_False() {
        testSkipModeAlwaysFalse(true,false,"a/b");
  
    }

    @Test
    public void RunModeIsReUseExecuteRootProjectIsRootPathOk_False() {
        testReuseModeTrue(true,true,"a/b");
    }
    
    @Test
    public void RunModeIsVsTestRootPojectIsRoot() {
        test(true,RunMode.RUNVSTEST,true,true,null);
    }
   
    @Test
    public void RunModeIsVsTestRootProjectIsNotRoot() {
        test(false,RunMode.RUNVSTEST,true,false,null);
    }
    
    @Test
    public void RunModeIsVsTestNotRootProjectIsRoot() {
        test(false,RunMode.RUNVSTEST,false,true,null);
    }
    public void testReuseModeTrue(boolean executeRoot,boolean isRoot,String path) {
        test(true,RunMode.REUSE,executeRoot,isRoot,path);       
    }
    public void testSkipModeAlwaysFalse(boolean executeRoot,boolean isRoot,String path) {
        test(false,RunMode.SKIP,executeRoot,isRoot,path);       
    }
    
    private void test(boolean expected,RunMode runMode,boolean executeRoot,boolean isRoot,String unitTestResultsPath) {
        propertiesHelper.setRunVsTest(true);
        propertiesHelper.setExecuteRoot(executeRoot);
        propertiesHelper.setUnitTestResultsPath(unitTestResultsPath);
        propertiesHelper.setRunMode(runMode);

                when(project.isRoot()).thenReturn(isRoot);
        boolean shouldExecute = resultsSensor.shouldExecuteOnProject(project);
        Assert.assertEquals(expected,shouldExecute);      
    }
    
}
