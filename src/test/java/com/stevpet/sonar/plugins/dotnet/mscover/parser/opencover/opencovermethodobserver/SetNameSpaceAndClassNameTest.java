package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover.opencovermethodobserver;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.sonar.api.utils.SonarException;

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
        assertEquals(null,actualNameSpaceName);
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
