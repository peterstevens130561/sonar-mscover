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
package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;


import com.stevpet.sonar.plugins.dotnet.mscover.property.TestRunnerTimeoutProperty;

public class TestRunnerTimeoutPropertyTests extends ConfigurationPropertyTestsBase<Integer> {
    

    @Before
    public void before() {
        setup(new TestRunnerTimeoutProperty());
        property = new TestRunnerTimeoutProperty(settings);
        
    }
    @Test
    public void notSpecificedExpectDefault() {
        int value = property.getValue();
        assertEquals("expect default",120,value);
    }
    
    
    @Test
    public void garbagepecificedExpectExecption() {
        setPropertyValue("groble");
        try {
            property.validate();
        } catch (InvalidPropertyValueException e) {
            return ; // this is expected
        } catch ( Exception e) {
            fail("expected InvalidPropertyException"); 
        }
    }
    
    @Test
    public void negativeSpecificedExpectExecption() {
        setPropertyValue("-1");
        try {
            property.validate();
        } catch (InvalidPropertyValueException e) {
            return ; // this is expected
        } catch ( Exception e) {
            fail("expected InvalidPropertyException"); 
        }
    }
    @Test
    public void validExpect() {
        setPropertyValue("5");
        int timeout = property.getValue();
        assertEquals(5,timeout);
    }
    @Test
    public void notSpecifiedShouldValidate() {
        try {
            property.validate();
        } catch (InvalidPropertyValueException e) {
            fail("should not have InvalidPropertyValueException, as setting is not required");
        } catch ( Exception e) {
            fail("no exception expected"); 
        }
        // fine
    }
    
}
