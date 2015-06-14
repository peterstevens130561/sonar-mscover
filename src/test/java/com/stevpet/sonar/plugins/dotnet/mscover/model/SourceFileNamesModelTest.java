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


public class SourceFileNamesModelTest {

    @Test
    public void CreateSourceFileModel_ShouldSucceed() {
        SourceFileNameRow model = new SourceFileNameRow() ;
        Assert.assertNotNull(model);
    }
    
    @Test
    public void SetFileID_ShouldGetSame() {
        SourceFileNameRow model = new SourceFileNameRow() ;
        String id = "1";
        model.setSourceFileID(id);
        Assert.assertEquals("should be same",1, (int)model.getSourceFileID().getId());
    }
    
    @Test
    public void SetFileName_ShouldGetSame() {
        SourceFileNameRow model = new SourceFileNameRow() ;
        String name = "a/b/c/";
        model.setSourceFileName(name);
        Assert.assertEquals(name, model.getSourceFileName());
    }   
    

   @Test
   public void SetNameField_ShouldBeSet() {
       SourceFileNameRow model = new SourceFileNameRow() ;
       String text= "a/b/c/";
       model.setSourceFileName(text);
       Assert.assertEquals(text, model.getSourceFileName());
       Assert.assertEquals(null,model.getSourceFileID());
   }
    
   @Test
   public void SetIDField_ShouldBeSet() {
       SourceFileNameRow model = new SourceFileNameRow() ;
       String id="1";
       model.setSourceFileID(id);
       Assert.assertEquals(1, (int)model.getSourceFileID().getId());
       Assert.assertNull(model.getSourceFileName());
   }
}
