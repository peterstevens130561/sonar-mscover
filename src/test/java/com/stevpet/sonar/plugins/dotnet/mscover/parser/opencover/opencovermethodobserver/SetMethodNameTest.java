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
