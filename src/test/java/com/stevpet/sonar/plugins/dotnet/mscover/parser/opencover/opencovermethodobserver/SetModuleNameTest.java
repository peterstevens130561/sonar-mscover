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
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover.opencovermethodobserver;

import static org.junit.Assert.*;

import org.junit.Test;

public class SetModuleNameTest {

    private OpenCoverMethodObserverSpy spy = new OpenCoverMethodObserverSpy();
    @Test
    public void normalModuleName_LastPath() {
        String name="C:\\Development\\Jewel.Release.Oahu\\JewelEarth\\Core\\ThinClient\\Common.UnitTest\\bin\\Debug\\BHI.JewelEarth.ThinClient.Common.dll";
        spy.setModuleName(name);
        String actual=spy.getModuleName();
        assertEquals("BHI.JewelEarth.ThinClient.Common.dll",actual);
    }
    
    @Test
    public void invalidName() {
        String name="BHI.JewelEarth.ThinClient.Common.dll";
        spy.setModuleName(name);
        assertTrue(spy.isSkipping());
    }
}
