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

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.SonarException;

public final class MethodId  {
    private String moduleName,namespaceName,className,methodName;
    private Pattern pattern = Pattern.compile("[^a-zA-Z0-9 \\._-]");
    private  Pattern moduleSuffixPattern = Pattern.compile("\\.(exe|dll)");

    public MethodId() { 
    }
    
    /**
     * Create a new unmutable methodid
     * @param moduleName
     * @param namespaceName
     * @param className
     * @param methodName
     */
    public MethodId(String moduleName, String namespaceName, String className, String methodName) {
        setModuleName(moduleName);
        setNamespaceName(namespaceName);
        setClassName(className);
        setMethodName(methodName);
    }
    
    private MethodId(MethodId methodId) {
        setModuleName(new String(methodId.getModuleName()));
        setNamespaceName(new String(methodId.getNamespaceName()));
        setClassName(new String(methodId.getClassName()));
        setMethodName(new String(methodId.getMethodName()));
    }

    public String getModuleName() {
        return moduleName;
    }

    private MethodId setModuleName(String moduleName) {
        this.moduleName=validate(moduleName);
        return this;

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

    private MethodId setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
        return this;
    }

    public String getClassName() {
        return className;
    }

    private MethodId setClassName(String className) {
        this.className = className;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    private MethodId setMethodName(String methodName) {
        this.methodName = removeArgumentList(methodName);
        if(StringUtils.isEmpty(this.methodName)) {
            throw new IllegalArgumentException("methodName");
        }
        return this;
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


    @Override
    public int hashCode() {
        return getId().hashCode();
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
        if(! (o instanceof MethodId)) {
            return false;
        }
        MethodId otherMethodId = (MethodId)o;
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
    
    public MethodId deepClone(){
       return new MethodId(this);
    }
}
