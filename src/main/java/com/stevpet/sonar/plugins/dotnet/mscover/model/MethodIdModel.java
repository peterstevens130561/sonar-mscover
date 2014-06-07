package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.utils.SonarException;

public final class MethodIdModel {
    private String moduleName,namespaceName,className,methodName;
    Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\._-]");
    Pattern moduleSuffixPattern = Pattern.compile("\\.(exe|dll)");

    public MethodIdModel() {
        
    }
    
    public MethodIdModel(String moduleName, String namespaceName, String className, String methodName) {
        setModuleName(moduleName);
        setNamespaceName(namespaceName);
        setClassName(className);
        setMethodName(methodName);
    }
    
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName=validate(moduleName);

    }

    /**
     * Due to a bug in vstest the modulename can contain some illegal information
     * after the dll, so everything after the .dll should be removed
     * @param name
     * @return modulename only
     */
    private String validate(String name) {
        Matcher suffixMatcher = moduleSuffixPattern.matcher(name);
        if(!suffixMatcher.find() ) {
            throw new SonarException("Modulename '" + name + "' must end on .dll or .exe");
        }
        
        String stripped = name.substring(0, suffixMatcher.end());
        Matcher matcher=pattern.matcher(stripped);
        if(matcher.find()) {
            String firstChar= name.substring(matcher.start(),matcher.end());
            throw new SonarException("invalid character(s) '" + firstChar + "'  in " + name);
        }
        return stripped;
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

    public static MethodIdModel create() {
        return new MethodIdModel();
    }
}
