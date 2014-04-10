package com.stevpet.sonar.plugins.dotnet.mscover.model;

import org.junit.Assert;
import org.junit.Test;


public class SourceFileNamesModelTest {

    @Test
    public void CreateSourceFileModel_ShouldSucceed() {
        SourceFileNamesModel model = new SourceFileNamesModel() ;
        Assert.assertNotNull(model);
    }
    
    @Test
    public void SetFileID_ShouldGetSame() {
        SourceFileNamesModel model = new SourceFileNamesModel() ;
        String id = "abcde";
        model.setSourceFileID(id);
        Assert.assertEquals(id, model.getSourceFileID());
    }
    
    @Test
    public void SetFileName_ShouldGetSame() {
        SourceFileNamesModel model = new SourceFileNamesModel() ;
        String name = "a/b/c/";
        model.setSourceFileName(name);
        Assert.assertEquals(name, model.getSourceFileName());
    }   
    
   @Test
   public void SetInvalidField_ShouldIgnore() {
       SourceFileNamesModel model = new SourceFileNamesModel() ;
       String name = "a/b/c/";
       model.setField("rubbish", name);
       Assert.assertNull(model.getSourceFileID());
   }

   @Test
   public void SetNameField_ShouldBeSet() {
       SourceFileNamesModel model = new SourceFileNamesModel() ;
       String text= "a/b/c/";
       String field = "SourceFileName";
       model.setField(field, text);
       Assert.assertEquals(text, model.getSourceFileName());
       Assert.assertNull(model.getSourceFileID());
   }
    
   @Test
   public void SetIDField_ShouldBeSet() {
       SourceFileNamesModel model = new SourceFileNamesModel() ;
       String text= "a/b/c/";
       String field = "SourceFileID";
       model.setField(field, text);
       Assert.assertEquals(text, model.getSourceFileID());
       Assert.assertNull(model.getSourceFileName());
   }
}
