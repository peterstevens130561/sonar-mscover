package com.stevpet.sonar.plugins.dotnet.mscover.model;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.SonarException;


public class UnitTestResultModel  {
    private String testId;
    private String testName;
    private String duration;
    private String outcome;
    private String relativeResultsDirectory;
    private String codeBase;
    private String className;
    private String moduleName;
    private String namespaceName;
    
    public String getModuleName() {
        return moduleName;
    }
    public String getNamespaceName() {
        return namespaceName;
    }

    
    public String getTestId() {
        return testId;
    }
    public void setTestId(String testId) {
        this.testId = testId;
    }
    public String getTestName() {
        return testName;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }
    public String getDuration() {
        return duration;
    }
    public String getCodeBase() {
        return codeBase;
    }
    public String getClassName() {
        return className;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getOutcome() {
        return outcome;
    }
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
    public String getRelativeResultsDirectory() {
        return relativeResultsDirectory;
    }
    public void setRelativeResultsDirectory(String relativeResultsDirectory) {
        this.relativeResultsDirectory = relativeResultsDirectory;
    }
    public void setCodeBase(String value) {
        this.codeBase = value;
        
    }
    
    public void setModuleFromCodeBase(String value) {
        if(StringUtils.isEmpty(value)) {
            throw new SonarException("module can't be null");
        }
        String[] parts = value.split("\\\\");
        moduleName=parts[parts.length-1];
    }
    
    
    public void setNamespaceNameFromClassName(String value) {
        if(StringUtils.isEmpty(value)) {
            throw new SonarException("namespacename can't be null");
        }
        int lastDot = value.lastIndexOf(".");
        if(lastDot==-1) {
            namespaceName=StringUtils.EMPTY;
        } else {
            namespaceName=value.substring(0, lastDot); 
        }
    }
    
    public void setClassName(String value) {
        this.className = value;
    }

    

}
