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
