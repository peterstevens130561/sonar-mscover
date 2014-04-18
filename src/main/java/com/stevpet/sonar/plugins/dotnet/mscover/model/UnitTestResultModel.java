package com.stevpet.sonar.plugins.dotnet.mscover.model;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.SonarException;


public class UnitTestResultModel  {
    private MethodIdModel methodId = new MethodIdModel();
    private String testId;
    private String duration;
    private String outcome;
    private String relativeResultsDirectory;
    private String codeBase;

    
    public String getModuleName() {
        return methodId.getModuleName();
    }
    public String getNamespaceName() {
        return methodId.getNamespaceName();
    }

    
    public String getTestId() {
        return testId;
    }
    public void setTestId(String testId) {
        this.testId = testId;
    }
    public String getTestName() {
        return methodId.getMethodName();
    }
    public void setTestName(String testName) {
        methodId.setMethodName(testName);
    }
    public String getDuration() {
        return duration;
    }
    public String getCodeBase() {
        return codeBase;
    }
    public String getClassName() {
        return methodId.getClassName();
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
        methodId.setModuleName(parts[parts.length-1]);
    }
    
    
    public void setNamespaceNameFromClassName(String value) {
        if(StringUtils.isEmpty(value)) {
            throw new SonarException("namespacename can't be null");
        }
        int lastDot = value.lastIndexOf(".");
        String namespaceName;
        if(lastDot==-1) {
            namespaceName=StringUtils.EMPTY;
        } else {
            namespaceName=value.substring(0, lastDot); 
        }
        methodId.setNamespaceName(namespaceName);
    }
    
    public void setClassName(String value) {
        if(StringUtils.isEmpty(value)) {
            throw new SonarException("className can't be null");
        }
        int lastDot = value.lastIndexOf(".");
        String className;
        if(lastDot==-1) {
            className=value;
        } else {
            className=value.substring(lastDot+1); 
        }
        methodId.setClassName(className);
    }
    public MethodIdModel getMethodId() {
        return methodId;
    }

    

}
