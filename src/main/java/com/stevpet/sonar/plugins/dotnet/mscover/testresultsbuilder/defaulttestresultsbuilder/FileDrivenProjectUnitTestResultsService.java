package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.model.VsTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public class FileDrivenProjectUnitTestResultsService implements ProjectUnitTestResultsService {
    private final static Logger LOG = LoggerFactory.getLogger(DefaultProjectUnitTestResultsService.class);
    private VsTestResults unitTestingResults;
    private SourceFileNameTable sourceFileNamesTable;
    private MethodToSourceFileIdMap methodToSourceFileIdMap;
    Function<MethodId,String> onNotFoundResolver;

    public FileDrivenProjectUnitTestResultsService(VsTestResults unitTestingResults, MethodToSourceFileIdMap map,
            SourceFileNameTable sourceFileNamesTable, Function<MethodId, String> resolver) {
        this.unitTestingResults = unitTestingResults;
        this.sourceFileNamesTable = sourceFileNamesTable;
        this.methodToSourceFileIdMap=map;
        this.onNotFoundResolver=resolver;
    }
    @Override
    public ProjectUnitTestResults mapUnitTestResultsToFile() {
        return null;
    }

    /**
     * get all methods that 
     */
}
