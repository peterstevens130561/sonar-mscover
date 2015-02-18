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
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover.opencovermethodobserver;

import static org.junit.Assert.*;

import org.junit.Test;

public class SetMethodNameTest {
    private OpenCoverMethodObserverSpy spy = new OpenCoverMethodObserverSpy();
    
    @Test
    public void StandardMethod_ShouldMatch() {
        String name="System.Uri BHI.JewelEarth.ThinClient.WinForms.RequestWrapper::get_Url()";
        spy.setMethodName(name);
        String actualMethodName=spy.getMethodName();
        assertEquals("get_Url",actualMethodName);       
    }
    
    @Test
    public void StandardMethodWithOneArg_ShouldMatch() {
        String name="System.Uri BHI.JewelEarth.ThinClient.WinForms.RequestWrapper::get_Url(System.Int64)";
        spy.setMethodName(name);
        String actualMethodName=spy.getMethodName();
        assertEquals("get_Url",actualMethodName);       
    }
    
    @Test
    public void Corrupted_ShouldGetException() {
        String name="System.Uri BHI.JewelEarth.ThinClient.WinForms.RequestWrapper:get_Url()";
        spy.setMethodName(name);
        assertTrue(spy.isSkipping());      
    }
    
    
}
