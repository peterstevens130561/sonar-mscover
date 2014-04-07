package com.stevpet.sonar.plugins.dotnet.mscover.model;

public class SourceFileNamesModel extends Model {
    
    private static final String SOURCE_FILE_NAME_ELEMENTNAME = "SourceFileName";
    private static final String SOURCE_FILE_ID_ELEMENTNAME = "SourceFileID";
    private String SourceFileID;
    private String SourceFileName;
    
    public  void setField(String name, String text) {
        if(name.equals(SOURCE_FILE_NAME_ELEMENTNAME)) {
            SourceFileID = text;
        } else if(name.equals(SOURCE_FILE_NAME_ELEMENTNAME)) {
            SourceFileName = text;
        }
              
    }

    public String getSourceFileID() {
        return SourceFileID;
    }
    
    public String getSourceFileName() {
        return SourceFileName;
    }

    @Override
    public String getFirstElement() {
        return SOURCE_FILE_ID_ELEMENTNAME;
    }

    @Override
    public String getLastElement() {
       return SOURCE_FILE_NAME_ELEMENTNAME;
    }
}
