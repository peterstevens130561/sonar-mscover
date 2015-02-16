package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;

public interface OpenCoverParserFactory {
    XmlParserSubject createOpenCoverParser(SonarCoverage registry);

    /**
     * Used to parse the opencover coverage file so that a unit test method can be linked to its source
     * file
     * @param map
     * @param sourceFileNamesRegistry
     * @return
     */
     XmlParserSubject createOpenCoverFileNamesParser(
            MethodToSourceFileIdMap map,
            SourceFileNameTable sourceFileNamesRegistry);

    XmlParserSubject createOpenCoverParser(SonarCoverage registry,
            MsCoverProperties msCoverProperties);
}
