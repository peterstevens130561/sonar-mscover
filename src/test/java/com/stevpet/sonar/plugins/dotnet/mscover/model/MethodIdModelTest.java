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

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;


public class MethodIdModelTest {
  
    @Test(expected=IllegalStateException.class)
    public void setModuleName_Invalid_Exception() {
    	new MethodId("!@( 12345:780.exe","validnamespace","MyClass","MyMethod");
    }
    
    @Test
    public void setModuleName_Space_Accepted() {
    	String NAME_WITH_SPACE = "JewelSuite Viewer.exe";
    	MethodId methodId=new MethodId(NAME_WITH_SPACE,"validnamespace","MyClass","MyMethod");
        assertEquals("name with space should be allowed",NAME_WITH_SPACE,methodId.getModuleName());
    }
    
    @Test
    public void setModuleName_ValidLC_Exception() {
        valid("john.dll");
    }
    
    @Test
    public void setModuleName_ValidUC_Pass() {
        valid("JOHN.dll");
      
    }
    
    @Test
    public void setModuleName_ValidDigit_Pass() {
        valid("1234.dll");

    }
    
    @Test
    public void setModuleName_ValidNameDigit_Pass() {
        valid("tfsblame.exe");

    }
    @Test
    public void setModuleName_AllCharacters_Pass() {
        valid("-_ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890abcdefghijklmnopqrstuvwxyz.dll");
    }
    private void valid(String name) {
    	MethodId methodId=new MethodId(name,"validnamespace","MyClass","MyMethod");
        Assert.assertEquals(name, methodId.getModuleName());
    }
}
