/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
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
