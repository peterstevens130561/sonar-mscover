/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.property.CoverageReaderThreadsProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;

public class CoverageReaderThreadsPropertyTests extends ConfigurationPropertyTestsBase<Integer> {
    @Before
    public void before() {
        setup(new CoverageReaderThreadsProperty());
        property = new CoverageReaderThreadsProperty(settings);
        
    }
    @Test
    public void notSpecificedExpectDefault() {
        checkDefaultOnNotSet(1);
    }
    
    
    @Test
    public void garbagepecificedExpectExecption() {
        checkExceptionOnNotInt();
    }
    
    @Test
    public void negativeSpecificedExpectExecption() {
        checkOutsideRangeInt(-1);
    }
    
    @Test
    public void tooMuchExpectExecption() {
        checkOutsideRangeInt(10);
    }
    @Test
    public void validLowExpect() {
        checkInRangeInt(1);

    }
    
    @Test
    public void validHighExpect() {
        checkInRangeInt(9);
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
