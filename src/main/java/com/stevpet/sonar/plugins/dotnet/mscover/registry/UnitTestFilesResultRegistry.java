package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestFileResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestResultModel;

public class UnitTestFilesResultRegistry {
    
    Map<String,UnitTestFileResultModel> unitTestFilesResultRegistry = new HashMap<String,UnitTestFileResultModel>();

    public void mapResults(UnitTestResultRegistry unitTestRegistry, MethodToSourceFileIdMap map) {
        Collection<UnitTestResultModel>unitTests=unitTestRegistry.values();
        for(UnitTestResultModel unitTest:unitTests) {
            map.setMethodId(unitTest.getMethodId());

            String fileId = map.getSourceFileID();

            if(!unitTestFilesResultRegistry.containsKey(fileId)) {
                unitTestFilesResultRegistry.put(fileId, new UnitTestFileResultModel());
            }
            UnitTestFileResultModel unitTestFileResults = unitTestFilesResultRegistry.get(fileId);
            unitTestFileResults.add(unitTest);
        }
    }

    public interface ForEachUnitTestFile {
        void execute(String fileId,UnitTestFileResultModel unitTest);
    }
    
    public void forEachUnitTestFile(ForEachUnitTestFile predicate) {
        for(Entry<String, UnitTestFileResultModel>entry: unitTestFilesResultRegistry.entrySet()) {
            String fileId = entry.getKey();
            UnitTestFileResultModel unitTest=entry.getValue();
            predicate.execute(fileId, unitTest);
        }
        
    }
}
