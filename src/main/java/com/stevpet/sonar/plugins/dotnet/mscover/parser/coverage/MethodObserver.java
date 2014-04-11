package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodRegistry;

public class MethodObserver extends BaseParserObserver {

    private MethodModel model = new MethodModel();
    private MethodRegistry registry;
    public MethodObserver() {
        setPattern("(Module/ModuleNameName)|" +
                        "(Module/NamespaceTable/Class/Method/MethodName)|" +
                        "(Module/NamespaceTable/Class/Method/Lines/(LnStart)|(SourceFileID))");

    }

    public void setRegistry(MethodRegistry registry) {
        this.registry = registry;
    }
    /**
     * put each field in the current object. When the final field is encountered the
     * current one is saved, and is cloned in a new one
     */
    public void observeElement(String name, String text) {

        model.setField(name,text);
        if(name.equals(model.getLastElement())) {
            registry.add(model);
            model=model.createClone();            
        }          
    }

}
