package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import java.io.File;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.FileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

public class VsTestFileNamesParser implements FileNamesParser {

    MethodToSourceFileIdMap methodToSourceFileIdMap;
    SourceFileNameTable sourceFileNameTable;

    @Override
    public void parse(File coverageFile) {
        methodToSourceFileIdMap = new MethodToSourceFileIdMap();
        sourceFileNameTable = new SourceFileNameTable();
        XmlParser xmlParser = new DefaultXmlParser();
        VsTestMethodObserver methodObserver = new VsTestMethodObserver();
        methodObserver.setRegistry(methodToSourceFileIdMap);
        xmlParser.registerObserver(methodObserver);

        VsTestSourceFileNamesObserver sourceFileNamesObserver = new VsTestSourceFileNamesObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNameTable);
        xmlParser.registerObserver(sourceFileNamesObserver);
        xmlParser.parseFile(coverageFile);
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
