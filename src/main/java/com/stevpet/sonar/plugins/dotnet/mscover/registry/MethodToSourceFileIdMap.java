package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIdModel;


public class MethodToSourceFileIdMap {
    
    private static final Logger LOG = LoggerFactory
            .getLogger(MethodToSourceFileIdMap.class);
    private Map<MethodIdModel,String> methodRegistry = new HashMap<MethodIdModel,String>();

   
    public void add(MethodIdModel methodId,String sourceFileId) {
        MethodIdModel methodClone = methodId.deepClone();
        methodRegistry.put(methodClone, sourceFileId);
    }
    
    public String get(MethodIdModel methodId) {
        if(methodId==null) {
            return null;
        }
        String fileId=methodRegistry.get(methodId);
        return fileId;
    }
 
    public String getLongestContainedMethod( MethodIdModel methodId) {
        if(methodId==null) {
            return null;
        }     
        MethodIdModel localMethodId = methodId.deepClone();
        String fileId;
        do {
            fileId=methodRegistry.get(localMethodId);
            if(fileId!= null) {
                break;
            }
            String name=localMethodId.getMethodName();
            String shorterName;
            if(name.matches("_.*")) {
                shorterName=name.substring(1);
            } else {
                shorterName=name.substring(0, name.length()-1);
            }
            localMethodId.setMethodName(shorterName);
        } while (!localMethodId.getMethodName().isEmpty());
        return fileId;
    }
   
    public int size() {
        return methodRegistry.size();
    }

    public void dumpMap() {
        File dump = new File("mapdump.txt") ;
        StringBuilder sb = new StringBuilder();
        for(Entry entry : methodRegistry.entrySet()) {
            String line = entry.getKey().toString() + " " + entry.getValue() + "\r\n";
            sb.append(line);
        }
        try {
            FileUtils.write(dump, sb.toString());
        } catch (IOException e) {
            throw new SonarException(e);
        }
        LOG.warn("dumped map to " + dump.getAbsolutePath());
    }



}
