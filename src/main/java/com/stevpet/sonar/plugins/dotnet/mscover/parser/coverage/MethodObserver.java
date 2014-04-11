package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodRegistry;

public class MethodObserver implements ParserObserver {
    private final Pattern pattern;
    private MethodModel model = new MethodModel();
    private MethodRegistry registry;
    public MethodObserver() {
        pattern = Pattern
                .compile("(Module/ModuleNameName)|" +
                        "(Module/NamespaceTable/Class/Method/MethodName)|" +
                        "(Module/NamespaceTable/Class/Method/Lines/(LnStart)|(SourceFileID))");

    }
    public boolean isMatch(String path) {
        Matcher matcher = pattern.matcher(path);
        return matcher.matches();        
    }
    public void setRegistry(MethodRegistry registry) {
        this.registry = registry;
    }
    /**
     * put each field in the current object. When the final field is encountered the
     * current one is saved, and is cloned in a new one
     */
    public void observe(String name, String text) {

        model.setField(name,text);
        if(name.equals(model.getLastElement())) {
            registry.add(model);
            model=model.createClone();            
        }          
    }

}
