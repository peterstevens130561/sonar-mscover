package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

public class MethodObserver extends BaseParserObserver {

    private MethodToSourceFileIdMap registry;
    public MethodObserver() {
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
        registry.setModuleName(value);
    }
    
    @ElementMatcher(elementName="NamespaceName")
    public void namespaceName(String value) {
        registry.setNamespaceName(value);
    }
    
    @ElementMatcher(elementName="ClassName")
    public void className(String value){
        registry.setClassName(value);
        
    }

    @ElementMatcher(elementName="MethodName") 
    public void methodName(String value) { 
        registry.setMethodName(value);
    }
    
    
    @ElementMatcher(elementName="SourceFileID") 
    public void sourceFileID(String value) {
        registry.setSourceFileID(value);
        registry.register();
    }
    

}
