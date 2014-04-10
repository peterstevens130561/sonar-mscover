package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNamesModel;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;


public class SourceFileNamesObserver implements ParserObserver {

    private final Pattern pattern;
    private SourceFileNamesRegistry registry ;
    private SourceFileNamesModel model;
    public SourceFileNamesObserver() {
        pattern = Pattern
                .compile("SourceFileNames/(SourceFileID|SourceFileName)");
    }
    public boolean isMatch(String path) {
        Matcher matcher = pattern.matcher(path);
        return matcher.matches();
    }

    public void observe(String name, String text) {
        if("SourceFileID".equals(name)) {
            model = new SourceFileNamesModel();
        }
        model.setField(name,text);
        if("SourceFileName".equals(name)) {
            registry.add(model.getSourceFileID(), model);
        }
    }
    public void setRegistry(SourceFileNamesRegistry registry) {
        this.registry=registry;
    }


}
