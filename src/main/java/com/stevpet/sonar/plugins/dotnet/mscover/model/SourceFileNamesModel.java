package com.stevpet.sonar.plugins.dotnet.mscover.model;

public class SourceFileNamesModel extends Model {
    
    private static final String SOURCE_FILE_NAME_ELEMENTNAME = "SourceFileName";
    private static final String SOURCE_FILE_ID_ELEMENTNAME = "SourceFileID";
    private String sourceFileID;
    private String sourceFileName;
    
    public  void setField(String name, String text) {
        if(name.equals(SOURCE_FILE_ID_ELEMENTNAME)) {
            sourceFileID = text;
        } else if(name.equals(SOURCE_FILE_NAME_ELEMENTNAME)) {
            sourceFileName = text;
        }
              
    }

    public String getSourceFileID() {
        return sourceFileID;
    }
    
    public String getSourceFileName() {
        return sourceFileName;
    }
    
    public void setSourceFileID(String value) {
        sourceFileID = value;
    }

    public void setSourceFileName(String value) {
        sourceFileName = value;
    }
}
