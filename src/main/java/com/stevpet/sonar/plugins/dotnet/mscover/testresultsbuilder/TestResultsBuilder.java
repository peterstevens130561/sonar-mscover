package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;

import java.io.File;

import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

public interface TestResultsBuilder extends BatchExtension {
    /**
     * given the coverage file and the test results file, build a repository of
     * test results
     * 
     * @param valid
     *            testResultsFile
     * @return
     */
    ProjectUnitTestResults parse(File testResultsFile, File coverageFile);
}
