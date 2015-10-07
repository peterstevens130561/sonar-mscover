package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import java.io.File;

import com.stevpet.sonar.plugins.common.api.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.FileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;

public class VsTestFileNamesParser implements FileNamesParser {

    MethodToSourceFileIdMap methodToSourceFileIdMap;
    SourceFileNameTable sourceFileNameTable;

    @Override
    public void parse(File coverageFile) {
        methodToSourceFileIdMap = new MethodToSourceFileIdMap();
        sourceFileNameTable = new SourceFileNameTable();
        XmlParserSubject parserSubject = new CoverageParserSubject();
        VsTestMethodObserver methodObserver = new VsTestMethodObserver();
        methodObserver.setRegistry(methodToSourceFileIdMap);
        parserSubject.registerObserver(methodObserver);

        VsTestSourceFileNamesObserver sourceFileNamesObserver = new VsTestSourceFileNamesObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNameTable);
        parserSubject.registerObserver(sourceFileNamesObserver);
        parserSubject.parseFile(coverageFile);
    }

    @Override
    public MethodToSourceFileIdMap getMethodToSourceFileIdMap() {
        return methodToSourceFileIdMap;
    }

    @Override
    public SourceFileNameTable getSourceFileNamesTable() {
        return sourceFileNameTable;
    }

}
