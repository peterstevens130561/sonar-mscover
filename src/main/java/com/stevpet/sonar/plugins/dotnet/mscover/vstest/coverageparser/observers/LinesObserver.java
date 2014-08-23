package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stevpet.sonar.plugins.dotnet.mscover.model.LineModel;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.LinesRegistry;


public class LinesObserver {
    private final Pattern pattern;
    private LinesRegistry registry ;
    private LineModel model = new LineModel();
    private boolean isActive=false;
    public LinesObserver() {
        pattern = Pattern
                .compile("Module/NamespaceTable/Class/Method/Lines/(LnStart|Coverage|SourceFileID)");
    }
    public boolean isMatch(String path) {
        Matcher matcher = pattern.matcher(path);
        return matcher.matches();
    }

    public void observe(String name, String text) {
        if(name.equals(model.getFirstElement())) {
            model = new LineModel();
            isActive=true;
        }

        if(!isActive) { 
            return;
        }
        model.setField(name,text);
        if(name.equals(model.getLastElement())) {
            registry.add(text, model);
            isActive=false;
        }
    }
    public void setRegistry(LinesRegistry registry) {
        this.registry=registry;
    }

}
