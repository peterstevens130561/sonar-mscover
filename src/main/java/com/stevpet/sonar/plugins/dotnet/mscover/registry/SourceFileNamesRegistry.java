package com.stevpet.sonar.plugins.dotnet.mscover.registry;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNamesModel;

public class SourceFileNamesRegistry implements Registry<SourceFileNamesModel> {
    Map<String,SourceFileNamesModel> map = new HashMap<String,SourceFileNamesModel>();


    public void add(String fileId, SourceFileNamesModel model) {
        map.put(fileId,model);
    }
    
    public SourceFileNamesModel get(String fileId) {
       return map.get(fileId);
    }

    public int size() {
        return map.size();
    }
    
    public Collection<SourceFileNamesModel> values() {
        return map.values();
    }

    /**
     * 
     * @param fileID
     * @return sourcefilename matching fileId, or null if not found
     */
    public String getSourceFileName(String fileID) {
        SourceFileNamesModel model = get(fileID);
        if(model==null) {
            return null;
        }
        return model.getSourceFileName();
    }

}
  