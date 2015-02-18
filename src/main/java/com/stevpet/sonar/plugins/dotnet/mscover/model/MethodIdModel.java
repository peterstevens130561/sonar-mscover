/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.utils.SonarException;

public final class MethodIdModel  {
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
    
    public MethodIdModel(MethodIdModel methodId) {
        setModuleName(new String(methodId.getModuleName()));
        setNamespaceName(new String(methodId.getNamespaceName()));
        setClassName(new String(methodId.getClassName()));
        setMethodName(new String(methodId.getMethodName()));
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
        this.methodName = removeArgumentList(methodName);
    }
    
    private String removeArgumentList(String method) {
        String result ;
        int pos=method.indexOf("(");
        if(pos>0) {
            result=method.substring(0, pos);
        } else {
            result=method;
        }
        return result;
    }
    public String getId() {
        String id=moduleName + ":" + namespaceName + "." + className + "!" + methodName;  
        return id.toLowerCase();
    }

    public static MethodIdModel create() {
        return new MethodIdModel();
    }

    @Override
    public int hashCode() {
        int result= getId().hashCode();
        return result;
    }
    
    @Override
    /**
     * Compare the methodIds, case of modulename is ignored, 
     * as it is a filename, of which the case is irrelevant
     */
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(null == o) {
            return false;
        }
        MethodIdModel otherMethodId = (MethodIdModel)o;
        return className.equals(otherMethodId.getClassName()) && 
                moduleName.equalsIgnoreCase(otherMethodId.getModuleName()) && 
                namespaceName.equals(otherMethodId.getNamespaceName()) &&
                methodName.equals(otherMethodId.getMethodName());      
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("module :").append(moduleName)
        .append(" namespace:").append(namespaceName)
        .append(" class:").append(className)
        .append(" method:").append(methodName)
        .append(" hash:").append(hashCode());
        return sb.toString();
    }
    
    public MethodIdModel deepClone(){
        MethodIdModel clone = new MethodIdModel(this);
        return clone;
    }
}
