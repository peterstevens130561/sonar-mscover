package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIdModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;

public class VsTestMethodToSourceFileIdMapObserver extends VsTestCoverageObserver {

    private MethodToSourceFileIdMap registry;
    private MethodIdModel methodId;
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
        methodId=new MethodIdModel();
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
