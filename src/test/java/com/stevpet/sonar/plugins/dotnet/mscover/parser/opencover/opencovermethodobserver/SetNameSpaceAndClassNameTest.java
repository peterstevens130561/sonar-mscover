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
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover.opencovermethodobserver;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SetNameSpaceAndClassNameTest {
    private OpenCoverMethodObserverSpy spy = new OpenCoverMethodObserverSpy();
    @Before
    public void before() {
        
    }
    
    /**
     * if the classname is <Module> then there are no following elements, it can be ignored
     */
    @Test
    public void Module_Ignored() {
        String name="<Module>";
        spy.setNamespaceAndClassName(name);
        String actualNameSpaceName=spy.getNameSpaceName();
        assertEquals("",actualNameSpaceName);
        assertFalse(spy.isSkipping());
    }
    @Test
    public void NameWithThree_NameSpaceIsFirstTwo() {
        String name="RequestWrapper.John.Fun";
        spy.setNamespaceAndClassName(name);
        String actualNameSpaceName=spy.getNameSpaceName();
        assertEquals("RequestWrapper.John",actualNameSpaceName);
        assertFalse(spy.isSkipping());
    }
    
    public void NameWithOne_Exception() {
        String name="RequestWrapper";
        spy.setNamespaceAndClassName(name);
        assertTrue(spy.isSkipping());
    }
    @Test
    public void NameWithTwo_NameSpaceIsFirst() {
        String name="RequestWrapper.John";
        spy.setNamespaceAndClassName(name);
        String actualNameSpaceName=spy.getNameSpaceName();
        assertEquals("RequestWrapper",actualNameSpaceName);
        assertFalse(spy.isSkipping());
    }
    
    @Test
    public void classNameWithThree_ShouldMatchLast() {
        String name="RequestWrapper.John.Fun";
        spy.setNamespaceAndClassName(name);
        String actualClassName=spy.getClassName();
        assertEquals("Fun",actualClassName);
        assertFalse(spy.isSkipping());
    }
    
    @Test
    public void classNameWithTwo_ShouldMatchLast() {
        String name="RequestWrapper.John";
        spy.setNamespaceAndClassName(name);
        String actualClassName=spy.getClassName();
        assertEquals("John",actualClassName);
        assertFalse(spy.isSkipping());
    }
}
