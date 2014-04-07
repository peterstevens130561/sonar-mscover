package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;

public class FileBlocksRegistry {
    private Map<String,FileBlocks> filesBlocks = new HashMap<String,FileBlocks>() ;
    
    public void setMap(Map<String,FileBlocks>map) {
        filesBlocks = map;
    }
    
    public void add(String fileId,BlockModel block) {
        if(!filesBlocks.containsKey(fileId)) {
            filesBlocks.put(fileId, new FileBlocks());
        }
        FileBlocks fileBlocks = filesBlocks.get(fileId);
        fileBlocks.add(block);
    }
    
    public FileBlocks get(String fileID) {
        return filesBlocks.get(fileID);
    }
    
    public Collection<FileBlocks> values() {
        return filesBlocks.values();
    }

}
