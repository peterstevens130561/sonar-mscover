package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;


public class VsTestSourceFileNamesToSourceFileNamesObserver extends VsTestCoverageObserver{

    private SourceFileNameTable registry ;
    private SourceFileNameRow model;
    public VsTestSourceFileNamesToSourceFileNamesObserver() {
        setPattern("SourceFileNames/(SourceFileID|SourceFileName)");
    }


    public void observeElement(String name, String text) {
        if("SourceFileID".equals(name)) {
            model = new SourceFileNameRow();
        }
        model.setField(name,text);
        if("SourceFileName".equals(name)) {
            registry.add(model.getSourceFileID(), model);
        }
    }
    public void setRegistry(SourceFileNameTable registry) {
        this.registry=registry;
    }


    @Override
    public void setVsTestRegistry(VsTestCoverageRegistry vsTestRegistry) {
        this.registry=vsTestRegistry.getSourceFileNameTable();
    }


}
