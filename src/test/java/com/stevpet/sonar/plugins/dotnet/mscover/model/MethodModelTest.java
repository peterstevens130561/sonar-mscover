package com.stevpet.sonar.plugins.dotnet.mscover.model;

import org.junit.Assert;
import org.junit.Test;

public class MethodModelTest {
    private String module = "unittests.dll";
    private String method = "john";
    private String fileID = "10" ;
    private String lnStart= "20";

    @Test
    public void createMethodModel_NoArguments_ExpectModel() {
        Model model = new MethodModel();
        Assert.assertNotNull(model);
    }
    
    @Test
    public void setMethodName_WithParenthesis_ExpectNone() {
        MethodModel model = new MethodModel();
        model.setMethod(method + "(int a)");
        Assert.assertEquals(method,model.getMethod());
    }
    
    @Test
    public void setModuleName_Random_ExpectSame() {
        MethodModel model = new MethodModel() ;
        model.setModule(module);
        Assert.assertEquals(module, model.getModule());
    }
    
    @Test
    public void setFileID_Random_ExpectSame() {
        MethodModel model = new MethodModel() ;
        model.setFileID(fileID);
        Assert.assertEquals(fileID, model.getFileID());       
    }
    
    @Test
    public void setLnStart_Random_ExpectSame() {
        MethodModel model = new MethodModel() ;

        model.setLnStart(lnStart);
        Assert.assertEquals(lnStart, model.getLnStart());             
    }
}
