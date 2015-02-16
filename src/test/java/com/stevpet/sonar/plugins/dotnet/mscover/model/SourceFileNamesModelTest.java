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
        int id = 1;
        model.setSourceFileID(id);
        Assert.assertEquals(id, model.getSourceFileID());
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
       String field = "SourceFileName";
       model.setField(field, text);
       Assert.assertEquals(text, model.getSourceFileName());
       Assert.assertEquals(0,model.getSourceFileID());
   }
    
   @Test
   public void SetIDField_ShouldBeSet() {
       SourceFileNameRow model = new SourceFileNameRow() ;
       String text= "1";
       String field = "SourceFileID";
       model.setField(field, text);
       Assert.assertEquals(1, model.getSourceFileID());
       Assert.assertNull(model.getSourceFileName());
   }
}
