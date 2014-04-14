package com.stevpet.sonar.plugins.dotnet.mscover.model;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.SonarException;

public class MethodModel extends Model {

    private String module;
    private String method;
    private String fileID;
    private String lnStart;
    
    
    public String getFileID() {
        return fileID;
    }
    public void setFileID(String fileID) {
        this.fileID = fileID;
    }
    public String getModule() {
        return module;
    }
    public String getMethod() {
        return method;
    }
    public String getLnStart() {
        return lnStart;
    }
    
    private void setSourceFileID(String name) {
        fileID = name;
        
    }
    /**
     * set the pure name of the method, discard anything from the (
     * @param name of method, possibly with the ( etc
     */
    void setMethod(String name) {
        if(!StringUtils.isEmpty(name)) {
            int endIndex = name.indexOf("(");
            if(endIndex>-1) {
                name=name.substring(0, endIndex);
            }
        }
        method=name;
        
    }
    void setLnStart(String name) {
        lnStart=name;
        
    }
     void setModule(String name) {
        module=name;
        
    }

    
    public MethodModel createClone()  {
         try {
            return  (MethodModel) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new SonarException(e);
        }
    }
}
