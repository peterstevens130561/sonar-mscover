package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.MethodRepository;


public class DefaultMethodRepository implements MethodRepository{

    Map<String,MethodIds> map = new HashMap<>();
    @Override
    public void addMethod(String fileId,MethodId methodId) {
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
}
