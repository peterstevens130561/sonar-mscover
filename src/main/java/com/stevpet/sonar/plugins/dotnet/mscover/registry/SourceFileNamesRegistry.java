package com.stevpet.sonar.plugins.dotnet.mscover.registry;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNamesModel;

public class SourceFileNamesRegistry  {
    private Map<Integer,SourceFileNamesModel> map = new HashMap<Integer,SourceFileNamesModel>();
    private Map<String,Integer> mapNameToId = new HashMap<String,Integer>();
    private int maxId=0;
    public void add(int i, SourceFileNamesModel model) {
        map.put(i,model);
        mapNameToId.put(model.getSourceFileName(),i);
        maxId = maxId>i?maxId:i;
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
    
    /**
     * makes sure that the file is in the table, returns it's id
     * @param fileName
     * @return
     */
    public int getSourceFileId(String fileName) {
        Integer id=mapNameToId.get(fileName);
        if(id==null) {
            id=getNewId();
            SourceFileNamesModel model = new SourceFileNamesModel(id,fileName);
            add(id,model);
        }
        return id;
    }

 
    private int getNewId() {
        return ++maxId;
    }
}
  