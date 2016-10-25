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
import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.ConfigurationPropertyTestsBase;

public class CoverageRootPropertyTests extends ConfigurationPropertyTestsBase<File>{

    @Before()
    public void before() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        setup(CoverageRootProperty.class);    
    }
    
    @Test
    public void undefinedExpectException() {
        try {
            property.getValue();
        } catch ( InvalidPropertyValueException e ) {
            return ;
        }
        fail("property not defined, expect exception");
    }
    
    @Test
    public void noParentExpectException() {
        this.setPropertyValue("dlddldl");
        super.expectInvalidPropetyValueExceptionOnGetValue();
    }
    
    @Test
    public void doesNotExisttException() {
        File dummy = TestUtils.getResource("CoverageRootProperty/CoverageRoot/dummy");
        File notExistingRoot = new File(dummy.getParentFile(),"bogus/bogus");
        this.setPropertyValue(notExistingRoot.getAbsolutePath());
        super.expectInvalidPropetyValueExceptionOnGetValue();
    }
    
    @Test
    public void existsShouldGet() {
        File dummy = TestUtils.getResource("CoverageRootProperty/CoverageRoot/dummy");
        File existingRoot = dummy.getParentFile();
        this.setPropertyValue(existingRoot.getAbsolutePath());      
        assertEquals("should get path",existingRoot,property.getValue());
    }
}
