package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.parser.ObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;

public class VsTestSourceFileNamesObserver extends BaseParserObserver {
    private SourceFileNameTable sourceFileNameTable;
    private String sourceFileID;

    @Override
    public void registerObservers(ObserverRegistrar registrar) {
        registrar.onElement("SourceFileID", this::sourceFileIDMatcher)
        .onElement("SourceFileName", this::sourceFileNameMatcher);
    }
    
    public void setRegistry(SourceFileNameTable sourceFileNameTable) {
        this.sourceFileNameTable = sourceFileNameTable;
    }

    public VsTestSourceFileNamesObserver() {
        setPattern("SourceFileNames/(SourceFileID|SourceFileName)");
    }

    public void sourceFileIDMatcher(String value) {
        sourceFileID = value;
    }

    public void sourceFileNameMatcher(String sourceFileName) {
        sourceFileNameTable.add(sourceFileID, sourceFileName);
    }



}
