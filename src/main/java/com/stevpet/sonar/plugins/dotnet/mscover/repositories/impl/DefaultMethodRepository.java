package com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl;

import java.util.HashMap;
import java.util.Map;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIds;
import com.stevpet.sonar.plugins.dotnet.mscover.model.impl.DefaultMethodIds;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.MethodRepository;


public class DefaultMethodRepository implements MethodRepository{

    Map<String,MethodIds> map = new HashMap<>();
    @Override
    public void add(String fileId,MethodId methodId) {
        MethodIds list= map.get(fileId);
        if(list==null) {
            list = new DefaultMethodIds();
            map.put(fileId, list);
        }
        list.add(methodId);       
    }
    
    /**
     * returns all methods in the given file, if none found then an empty list is returned
     * 
     * @param fileId
     * @return
     */
    @Override
    public MethodIds getMethods(String fileId) {
        MethodIds list= map.get(fileId);
        if(list==null) {
            list = new DefaultMethodIds();
        }
        return list;
    }

    @Override
    public String getFileId(MethodId methodId) {
        // TODO Auto-generated method stub
        return null;
    }
}
