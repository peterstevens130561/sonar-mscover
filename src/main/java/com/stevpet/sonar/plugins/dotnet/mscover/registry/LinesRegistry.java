package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.util.HashMap;
import java.util.Map;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLines;
import com.stevpet.sonar.plugins.dotnet.mscover.model.LineModel;

public class LinesRegistry  {
    Map<String,FileLines> fileLines = new HashMap<String,FileLines>();
    
    public void add(String fileId, LineModel line) {
        if(!fileLines.containsKey(fileId)) {
            fileLines.put(fileId,new FileLines());
        }
        fileLines.get(fileId).add(line);
    }


    public FileLines get(String fileId) {
        return fileLines.get(fileId);
    }

    public int size() {
        return fileLines.size();
    }

}
