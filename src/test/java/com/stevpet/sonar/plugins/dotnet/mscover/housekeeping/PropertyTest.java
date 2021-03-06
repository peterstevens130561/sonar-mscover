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
package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class PropertyTest {

    
    @Test
    public void normalProperty() {
        Property property = new Property("key=value");
        assertEquals("key",property.getKey());
        assertEquals("value",property.getValue());
    }
    
    @Test
    public void doubleEqualsProperty() {
        Property property = new Property("key=value=garbage");
        assertEquals("key",property.getKey());
        assertEquals("value=garbage",property.getValue());
    }
    
    @Test
    public void funnyLine() {
        Property property = new Property("ExecutablePath=C:\\users\\stevpet\\AppData\\Local\\Apps\\OpenCover\\OpenCover.Console.Exe");
        assertEquals("ExecutablePath",property.getKey());
        assertEquals("C:\\users\\stevpet\\AppData\\Local\\Apps\\OpenCover\\OpenCover.Console.Exe",property.getValue());
    }
    
    @Test
    public void invalidLine() {
        try {
            new Property("MissingSeperator");
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Excpected IllegalArgumentException");
    }
}
