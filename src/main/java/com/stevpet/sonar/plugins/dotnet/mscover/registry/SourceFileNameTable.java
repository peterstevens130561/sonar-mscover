package com.stevpet.sonar.plugins.dotnet.mscover.registry;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;

public class SourceFileNameTable  {
    private Map<Integer,SourceFileNameRow> rows = new HashMap<Integer,SourceFileNameRow>();
    private Map<String,Integer> mapNameToId = new HashMap<String,Integer>();
    private int maxId=0;
    public void add(int i, SourceFileNameRow model) {
        rows.put(i,model);
        mapNameToId.put(model.getSourceFileName(),i);
        maxId = maxId>i?maxId:i;
    }
    
    public SourceFileNameRow get(String fileId) {
       return rows.get(fileId);
    }

    public int size() {
        return rows.size();
    }
    
    public Collection<SourceFileNameRow> values() {
        return rows.values();
    }

    /**
     * 
     * @param fileID
     * @return sourcefilename matching fileId, or null if not found
     */
    public String getSourceFileName(String fileID) {
        SourceFileNameRow model = get(fileID);
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
            id= ++maxId;
            SourceFileNameRow model = new SourceFileNameRow(id,fileName);
            add(id,model);
        }
        return id;
    }


}
  