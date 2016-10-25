package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.VsTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

import java.util.function.Function;
public class DefaultProjectUnitTestResultsService implements ProjectUnitTestResultsService {
    private final static Logger LOG = LoggerFactory.getLogger(DefaultProjectUnitTestResultsService.class);
    private VsTestResults unitTestingResults;
    private SourceFileNameTable sourceFileNamesTable;
    private MethodToSourceFileIdMap methodToSourceFileIdMap;
    Function<MethodId,String> onNotFoundResolver;

    public DefaultProjectUnitTestResultsService(VsTestResults unitTestingResults, MethodToSourceFileIdMap map,SourceFileNameTable sourceFileNamesTable) {
        this.unitTestingResults = unitTestingResults;
        this.sourceFileNamesTable = sourceFileNamesTable;
        this.methodToSourceFileIdMap=map;
    }
    public DefaultProjectUnitTestResultsService(VsTestResults unitTestingResults, MethodToSourceFileIdMap map,
            SourceFileNameTable sourceFileNamesTable, Function<MethodId, String> resolver) {
        this.unitTestingResults = unitTestingResults;
        this.sourceFileNamesTable = sourceFileNamesTable;
        this.methodToSourceFileIdMap=map;
        this.onNotFoundResolver=resolver;
    }
    @Override
    public ProjectUnitTestResults mapUnitTestResultsToFile() {
        Map<String,ClassUnitTestResult> unitTestFilesResultRegistry = new HashMap<String,ClassUnitTestResult>();

        Collection<UnitTestMethodResult>unitTests=unitTestingResults.values();
        for(UnitTestMethodResult unitTest:unitTests) {
            MethodId methodId=unitTest.getMethodId();
            String fileId = methodToSourceFileIdMap.get(methodId);
            String filePath=null;
            if(fileId!=null) {
                 filePath = sourceFileNamesTable.getSourceFileName(fileId);
            }

            if (filePath==null && onNotFoundResolver!=null) {
                filePath=onNotFoundResolver.apply(methodId);
            }
            if(filePath==null) {
                LOG.warn("Could not find filename for method " + methodId + "");
                continue;
            }

            if(!unitTestFilesResultRegistry.containsKey(filePath)) {
                unitTestFilesResultRegistry.put(filePath, new ClassUnitTestResult(new File(filePath)));
            }
            ClassUnitTestResult classUnitTestResult=unitTestFilesResultRegistry.get(filePath);
            classUnitTestResult.add(unitTest);
            
        }
        ProjectUnitTestResults projectUnitTestResults = new ProjectUnitTestResults();
        projectUnitTestResults.addAll(unitTestFilesResultRegistry.values());
        return projectUnitTestResults;
    }

}
