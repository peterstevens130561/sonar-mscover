package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.util.HashMap;
import java.util.Map;

public class MethodToSourceFileIdMap {
    private Map<String,String> methodRegistry= new HashMap<String,String>();
    private String moduleName;
    private String clazz;
    private String namespace;
    private String method;
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
        namespace = value;
    }
    
    /**
     * set the class to register / get
     * @param value
     */
    public void setClassName(String value) {
        clazz = value;
    }
    
    /**
     * set the method to register / get
     * @param value
     */
    public void setMethodName(String value) {
        method=removeArgumentList(value);
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
        String key=createKey() ;
        //methods may be overloaded, thus it may happen that
        //it appears multiple times. and thus we should not check
        methodRegistry.put(key, sourceFileID);
        registered=true;
    }

    /**
     * get the sourcefileid matching the set attributes
     * @return
     */
    public String getSourceFileID() {
        String key=createKey() ;
        return methodRegistry.get(key);
    }
    
    private String createKey() {
        return moduleName + ":" + namespace + "." + clazz + "!" + method;
    }
    
    public int size() {
        return methodRegistry.size();
    }

}
