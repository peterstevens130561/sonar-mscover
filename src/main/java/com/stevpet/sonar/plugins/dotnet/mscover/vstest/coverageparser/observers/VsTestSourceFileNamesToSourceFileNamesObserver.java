package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNamesModel;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestRegistry;


public class VsTestSourceFileNamesToSourceFileNamesObserver extends VsTestCoverageObserver{

    private SourceFileNamesRegistry registry ;
    private SourceFileNamesModel model;
    public VsTestSourceFileNamesToSourceFileNamesObserver() {
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


    @Override
    public void setVsTestRegistry(VsTestRegistry vsTestRegistry) {
        this.registry=vsTestRegistry.getSourceFileNamesRegistry();
    }


}
