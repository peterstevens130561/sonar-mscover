package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public interface CoverageParser {
    /**
     * @param file to parse
     * @param registry in which the results will be stored
     */
    void parse(SonarCoverage registry, File file);
}
