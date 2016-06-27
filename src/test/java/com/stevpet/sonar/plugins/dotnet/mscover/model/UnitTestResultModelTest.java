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
package com.stevpet.sonar.plugins.dotnet.mscover.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;


public class UnitTestResultModelTest {
    private UnitTestMethodResult model;
    @Before
    public void before() {
        model = new UnitTestMethodResult();
    }
    
    @Test
    public void createModel() {
        Assert.assertNotNull(model);
    }
    
    @Test
    public void setTestId() {
        String expected="abcdefg";
        String actual=model.getTestId();
        Assert.assertNull(actual);
        model.setTestId(expected);
        actual=model.getTestId();
        Assert.assertEquals(expected, actual);
        
        
    }
    
    @Test(expected=IllegalStateException.class)
    public void setNamespaceNameFromClassName_Null() {
        String className = "";
        model.setNamespaceNameFromClassName(className);
    }
    @Test
    public void setNamespaceNameFromClassName_Empty() {
        String namespace = "" ;
        String className = "john";
        model.setNamespaceNameFromClassName(className);
        String actual = model.getNamespaceName();
        Assert.assertEquals(namespace, actual);
    }
    
    @Test
    public void setNamespaceNameFromClassName_Normal() {
        String namespace = "com.bhi.jewelsuite" ;
        String className = namespace + ".john";
        model.setNamespaceNameFromClassName(className);
        String actual = model.getNamespaceName();
        Assert.assertEquals(namespace, actual);
    }
    
    @Test(expected=IllegalStateException.class)
    public void setClassName_Empty() {
        String className = "";
        model.setClassName(className);

    }
    
    @Test
    public void setClassNAme_Normal() {
        String className = "john";
        String path = "bhi.com." + className;
        model.setClassName(path);
        String actual = model.getClassName();
        Assert.assertEquals(className, actual);
    }
    
    @Test
    public void setClassNAme_NoNamespace() {
        String className = "john";
        String path = className;
        model.setClassName(path);
        String actual = model.getClassName();
        Assert.assertEquals(className, actual);
    }
    
    @Test(expected=IllegalStateException.class)
    public void setModuleFromCodeBase_Empty() {
        testCodeBase("","");

    }
    @Test 
    public void setModuleFromCodeBase_ForwardSlash() {
        testCodeBase("C:/dev/aap/","test.dll");

    }
    
    @Test
    public void setModuleFromCodeBase_BackwardSlash(){
        testCodeBase("C:\\dev\\aap\\","test.dll");
    }
    
    public void testCodeBase(String dir, String module) {
        String path = dir + module;
        model.setModuleFromCodeBase(path);
        String actual = model.getModuleName();
        Assert.assertEquals(module, actual);;
    }
    
    @Test
    public void getMethodId() {
        String codeBase = "C:/dev/unittest.dll";
        String classPath = "bhi.com.test";
        model.setTestName("testName");
        model.setModuleFromCodeBase(codeBase);
        model.setNamespaceNameFromClassName(classPath);
        model.setClassName(classPath);
        MethodId methodId = model.getMethodId();
        Assert.assertNotNull(methodId);
        Assert.assertEquals("unittest.dll",methodId.getModuleName());
        Assert.assertEquals("bhi.com", methodId.getNamespaceName());
        Assert.assertEquals("test",methodId.getClassName());
        Assert.assertEquals("testName",methodId.getMethodName());
        
    }
    
}
