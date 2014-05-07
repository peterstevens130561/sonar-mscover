package com.stevpet.sonar.plugins.dotnet.mscover.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.utils.SonarException;

public class MethodIdModelTest {

    MethodIdModel methodIdModel ;
    @Before
    public void before() {
        methodIdModel = MethodIdModel.create();
    }
    
    @Test(expected=SonarException.class)
    public void setModuleName_Invalid_Exception() {
        methodIdModel.setModuleName("!@( 12345:780");
    }
    
    @Test
    public void setModuleName_ValidLC_Exception() {
        valid("john.dll");
    }
    
    @Test
    public void setModuleName_ValidUC_Pass() {
        valid("JOHN.dll");
      
    }
    
    @Test
    public void setModuleName_ValidDigit_Pass() {
        valid("1234.dll");

    }
    
    @Test
    public void setModuleName_ValidNameDigit_Pass() {
        valid("tfsblame.exe");

    }
    @Test
    public void setModuleName_AllCharacters_Pass() {
        valid("-_ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890abcdefghijklmnopqrstuvwxyz.dll");
    }
    private void valid(String name) {
        methodIdModel.setModuleName(name);
        Assert.assertEquals(name, methodIdModel.getModuleName());
    }
}
