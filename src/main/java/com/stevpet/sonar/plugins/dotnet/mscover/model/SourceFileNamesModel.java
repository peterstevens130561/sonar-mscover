package com.stevpet.sonar.plugins.dotnet.mscover.model;

public class SourceFileNamesModel extends Model {
    
    private static final String SOURCE_FILE_NAME_ELEMENTNAME = "SourceFileName";
    private static final String SOURCE_FILE_ID_ELEMENTNAME = "SourceFileID";
    private int sourceFileID;
    private String sourceFileName;
    
    public  void setField(String name, String text) {
        if(name.equals(SOURCE_FILE_ID_ELEMENTNAME)) {
            sourceFileID = Integer.parseInt(text);
        } else if(name.equals(SOURCE_FILE_NAME_ELEMENTNAME)) {
            sourceFileName = text;
        }
              
    }

    public SourceFileNamesModel(int id,String name) {
        sourceFileID=id;
        sourceFileName=name;
    }
    public SourceFileNamesModel() {
    }

    public int getSourceFileID() {
        return sourceFileID;
    }
    
    public String getSourceFileName() {
        return sourceFileName;
    }
    
    public void setSourceFileID(int value) {
        sourceFileID = value;
    }

    public void setSourceFileName(String value) {
        sourceFileName = value;
    }
}
