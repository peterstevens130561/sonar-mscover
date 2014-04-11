package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNamesModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;


public class SourceFileNamesObserver extends BaseParserObserver {

    private SourceFileNamesRegistry registry ;
    private SourceFileNamesModel model;
    public SourceFileNamesObserver() {
        setPattern("SourceFileNames/(SourceFileID|SourceFileName)");
    }


    public void observeElement(String name, String text) {
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
