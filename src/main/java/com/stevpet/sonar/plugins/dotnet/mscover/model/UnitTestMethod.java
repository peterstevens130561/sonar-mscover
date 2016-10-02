package com.stevpet.sonar.plugins.dotnet.mscover.model;

public class UnitTestMethod {
    private final MethodId methodId ;
    private String fileId ;
    private UnitTestMethodResult result ;
    
    public UnitTestMethod(MethodId methodId) {
        this.methodId=methodId;
    }
    
    public void setFileId(String fileId) {
        this.fileId=fileId;
    }
    
    public String getFileId() {
        return fileId;
    }
    
    public UnitTestMethodResult getMethodResult() {
        return result;
    }
    @Override
    public int hashCode() {
        return methodId.hashCode();
    }
    
    @Override
    /**
     * Compare the methodIds, case of modulename is ignored, 
     * as it is a filename, of which the case is irrelevant
     */
    public boolean equals(Object o) {
        return methodId.equals(o);
    }
}
