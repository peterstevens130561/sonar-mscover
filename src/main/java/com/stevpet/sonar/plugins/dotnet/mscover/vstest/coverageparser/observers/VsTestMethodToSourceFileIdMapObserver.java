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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;

public class VsTestMethodToSourceFileIdMapObserver extends VsTestCoverageObserver {

    private MethodToSourceFileIdMap registry;
    private MethodId methodId;
    public VsTestMethodToSourceFileIdMapObserver() {
        setPattern("(Module/ModuleName)|" +
                "(Module/NamespaceTable/NamespaceName)|" +
                "(Module/NamespaceTable/Class/ClassName)|" +
                        "(Module/NamespaceTable/Class/Method/MethodName)|" +
                        "(Module/NamespaceTable/Class/Method/Lines/(SourceFileID))");

    }

    public void setRegistry(MethodToSourceFileIdMap registry) {
        this.registry = registry;
    }
 
    @ElementMatcher(elementName="ModuleName")
    public void moduleName(String value) {
        methodId=new MethodId();
        methodId.setModuleName(value);
    }
    
    @ElementMatcher(elementName="NamespaceName")
    public void namespaceName(String value) {
        methodId.setNamespaceName(value);
    }
    
    @ElementMatcher(elementName="ClassName")
    public void className(String value){
        methodId.setClassName(value);
    }

    @ElementMatcher(elementName="MethodName") 
    public void methodName(String value) { 
        methodId.setMethodName(value);
    }
    
    
    @ElementMatcher(elementName="SourceFileID") 
    public void sourceFileID(String sourceFileId) {
        registry.add(methodId,sourceFileId);
    }

    @Override
    public void setVsTestRegistry(VsTestCoverageRegistry vsTestRegistry) {
        registry=vsTestRegistry.getMethodToSourceFileIdMap();
    }
    

}
