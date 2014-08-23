package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNamesModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;


public class CoverageSourceFileNamesObserver extends BaseParserObserver {

    private SourceFileNamesRegistry registry ;
    private SourceFileNamesModel model;
    public CoverageSourceFileNamesObserver() {
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
