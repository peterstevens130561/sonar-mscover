/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.ConfigurationPropertyTestsBase;

public class ExcludeProjectsPropertyTests extends ConfigurationPropertyTestsBase<List<String>> {

    @Before
    public void before() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        setup(ExcludeProjectsProperty.class);    
    }
    

    
    @Test
    public void undefinedExpectEmptyArray() {
        List<String> projects = property.getValue();
        assertNotNull("should be list",projects);
        assertEquals("should be empty list",0,projects.size());
    }
    
    @Test
    public void oneElementExpectInArray() {
        this.setPropertyValue("myproject");
        List<String>projects = property.getValue();
        assertEquals("should be list with one element",1,projects.size());
        assertEquals("myproject",projects.get(0));
    }
    
    @Test
    public void twoElementExpectInArray() {
        this.setPropertyValue("myproject,secondproject");
        List<String>projects = property.getValue();
        assertEquals("should be list with one element",2,projects.size());
        assertEquals("myproject",projects.get(0));
        assertEquals("secondproject",projects.get(1));
    }
    
    @Test
    public void twoElementExpectFound() {
        this.setPropertyValue("myproject,secondproject");
        List<String>projects = property.getValue();
        assertTrue(projects.contains("myproject"));
        assertTrue(projects.contains("secondproject"));
        assertFalse(projects.contains("funny"));
    }
}
