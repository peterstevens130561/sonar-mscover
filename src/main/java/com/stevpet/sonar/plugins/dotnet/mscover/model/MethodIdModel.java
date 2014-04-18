package com.stevpet.sonar.plugins.dotnet.mscover.model;

public class MethodIdModel {
    private String moduleName,namespaceName,className,methodName;

    public MethodIdModel() {
        
    }
    
    public MethodIdModel(String moduleName, String namespaceName, String className, String methodName) {
        this.moduleName = moduleName;
        this.namespaceName = namespaceName;
        this.className = className;
        this.methodName = methodName;
    }
    
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public String getId() {
        String id=moduleName + ":" + namespaceName + "." + className + "!" + methodName;  
        return id.toLowerCase();
    }
}
