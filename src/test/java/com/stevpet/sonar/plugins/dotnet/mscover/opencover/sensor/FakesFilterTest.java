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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FilenameFilter;

import org.junit.Test;

public class FakesFilterTest {
    FilenameFilter fakesFilter = new FakesFilter();
    @Test
    public void acceptInvalid_False() {
        assertFalse(fakesFilter.accept(null, "MsCorLib.4.0.0.0.dll"));
    }
    
    @Test
    public void acceptUpperFake_True() {
        assertTrue(fakesFilter.accept(null, "EnvDTE.8.0.0.0.Fakes.dll"));
    }
    
    @Test
    public void acceptLowerFake_True() {
        assertTrue(fakesFilter.accept(null, "EnvDTE.8.0.0.0.fakes.dll"));
    }
    
    @Test
    public void acceptLowerFakeXml_True() {
        assertTrue(fakesFilter.accept(null, "EnvDTE.8.0.0.0.fakes.xml"));
    }
    
    @Test
    public void fakeWithoutDot_False() {
        assertFalse(fakesFilter.accept(null, "fakeAssembly.dll"));       
    }
}
