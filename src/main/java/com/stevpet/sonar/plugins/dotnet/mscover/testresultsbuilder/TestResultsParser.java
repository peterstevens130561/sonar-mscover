package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;

import java.io.File;

import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;

public interface TestResultsParser extends BatchExtension {

    /**
     * parse a test results file to get {@link UnitTestRegistry}
     * 
     * @param coverageFile
     */
    void parse(File coverageFile);

    /**
     * get the {@link UnitTestRegistry}
     * 
     * @return
     */
    UnitTestRegistry getUnitTestRegistry();

}