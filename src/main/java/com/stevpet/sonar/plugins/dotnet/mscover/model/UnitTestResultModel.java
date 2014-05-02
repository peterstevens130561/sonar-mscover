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
    private String message = StringUtils.EMPTY;
    private String stackTrace;

    
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
    
    /**
     * set the module to the last segment in the codeBase
     * @param path to the dll, may use / or \ in the path, 
     */
    public void setModuleFromCodeBase(String codeBase) {
        if(StringUtils.isEmpty(codeBase)) {
            throw new SonarException("module can't be null");
        }
        codeBase=codeBase.replace("\\","/");
        String[] parts = codeBase.split("/");
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
    public void setMessage(String value) {
        message=value;     
    }

    public String getMessage() {
        return message;
    }
    public void setStackTrace(String value) {
       stackTrace=value; 
    }
    
    public String getStackTrace() {
        return stackTrace;
    }
    

}
