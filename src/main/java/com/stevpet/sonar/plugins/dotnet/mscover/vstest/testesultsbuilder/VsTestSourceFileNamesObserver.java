package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.parser.ObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;

public class VsTestSourceFileNamesObserver extends BaseParserObserver {
    private SourceFileNameTable sourceFileNameTable;
    private String sourceFileID;

    public VsTestSourceFileNamesObserver() {
        setPattern("SourceFileNames/(SourceFileID|SourceFileName)");
    }
    
    @Override
    public void registerObservers(ObserverRegistrar registrar) {
        registrar.inPath("SourceFileNames")
            .onElement("SourceFileID", value -> sourceFileID=value)
            .onElement("SourceFileName", value -> sourceFileNameTable.add(sourceFileID, value));
    }
    
    public void setRegistry(SourceFileNameTable sourceFileNameTable) {
        this.sourceFileNameTable = sourceFileNameTable;
    }

}
