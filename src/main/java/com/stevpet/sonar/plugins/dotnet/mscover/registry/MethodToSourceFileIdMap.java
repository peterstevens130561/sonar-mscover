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
    private Map<String,String> methodRegistry= new HashMap<String,String>();
    private String moduleName;
    private String className;
    private String namespaceName;
    private String methodName;
    private String sourceFileID;
    private boolean registered;


    /**
     * set the modulename to register / get
     * @param value
     */
    public void setModuleName(String value) {
        moduleName = value;
    }
    
    /**
     * set the namespace to register / get
     * @param value
     */
    public void setNamespaceName(String value) {
        namespaceName = value;
    }
    
    /**
     * set the class to register / get
     * @param value
     */
    public void setClassName(String value) {
        className = value;
    }
    
    /**
     * set the method to register / get
     * @param value
     */
    public void setMethodName(String value) {
        methodName=removeArgumentList(value);
        registered=false;
    }
    
    private String removeArgumentList(String method) {
        String result ;
        int pos=method.indexOf("(");
        if(pos>0) {
            result=method.substring(0, pos);
        } else {
            result=method;
        }
        return result;
    }
    
    /**
     * set the sourcefileid to register
     * @param value
     */
    public void setSourceFileID(String value) {
        sourceFileID=value;
    }
    
    public void register() {
        if(registered) {
            return ;
        }
        MethodIdModel model = new MethodIdModel(moduleName,namespaceName,className,methodName);
        //methods may be overloaded, thus it may happen that
        //it appears multiple times. and thus we should not check
        String key=model.getId();
        if(key==null) {
            throw new SonarException("model key == null!");
        }
        methodRegistry.put(model.getId(), sourceFileID);
        registered=true;
    }

    /**
     * get the sourcefileid matching the set attributes
     * @return
     */
    public String getSourceFileID() {
        MethodIdModel key = new MethodIdModel(moduleName,namespaceName,className,methodName);
        String id = key.getId();
        return methodRegistry.get(id);
    }
    

    
    public int size() {
        return methodRegistry.size();
    }

    public void setMethodId(MethodIdModel methodId) {
        this.moduleName = methodId.getModuleName();
        this.namespaceName = methodId.getNamespaceName();
        this.className = methodId.getClassName();
        this.methodName = methodId.getMethodName();
        
    }

    public void dumpMap() {
        File dump = new File("mapdump.txt") ;
        StringBuilder sb = new StringBuilder();
        for(Entry entry : methodRegistry.entrySet()) {
            String line = entry.getKey() + " " + entry.getValue() + "\r\n";
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
