package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;








import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIdModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestFileResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestResultModel;

public class UnitTestFilesResultRegistry {
    Logger LOG = LoggerFactory.getLogger(UnitTestFilesResultRegistry.class);
    Map<String,UnitTestFileResultModel> unitTestFilesResultRegistry = new HashMap<String,UnitTestFileResultModel>();

    public void mapResults(UnitTestResultRegistry unitTestRegistry, MethodToSourceFileIdMap map) {
        Collection<UnitTestResultModel>unitTests=unitTestRegistry.values();
        for(UnitTestResultModel unitTest:unitTests) {
            MethodIdModel methodId=unitTest.getMethodId();
            map.setMethodId(methodId);

            String fileId = map.getSourceFileID();
            if(fileId==null) {
                map.dumpMap();
                String msg = createPrettyMessage(methodId);
                LOG.error(msg);
                throw new MsCoverCanNotFindSourceFileForMethodException(msg);
            }
            if(!unitTestFilesResultRegistry.containsKey(fileId)) {
                unitTestFilesResultRegistry.put(fileId, new UnitTestFileResultModel());
            }
            UnitTestFileResultModel unitTestFileResults = unitTestFilesResultRegistry.get(fileId);
            unitTestFileResults.add(unitTest);
        }
    }

    private String createPrettyMessage(MethodIdModel methodId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Can't find sourcefile for the following method\n");
        sb.append("One known cause is that the method is inherited");
        sb.append("\nassembly  :").append(methodId.getModuleName());
        sb.append("\nnamespace :").append(methodId.getNamespaceName());
        sb.append("\nclass     :").append(methodId.getClassName());
        sb.append("\nmethod    :").append(methodId.getMethodName());
        return sb.toString();
    }

    public interface ForEachUnitTestFile {
        void execute(String fileId,UnitTestFileResultModel unitTest);
    }
    
    public void forEachUnitTestFile(ForEachUnitTestFile action) {
        for(Entry<String, UnitTestFileResultModel>entry: unitTestFilesResultRegistry.entrySet()) {
            String fileId = entry.getKey();
            UnitTestFileResultModel unitTest=entry.getValue();
            action.execute(fileId, unitTest);
        }
        
    }
}
