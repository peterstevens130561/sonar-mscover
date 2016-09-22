package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.MethodRepository;

public class DefaultMethodRepository implements MethodRepository{

    @Override
    public void addMethod(String fileId,MethodId methodId) {
        
    }
    
    /**
     * returns all methods in the given file, if none found then an empty list is returned
     * 
     * @param fileId
     * @return
     */
    @Override
    public List<MethodId> getMethods(String fileId) {
        List<MethodId> result = new ArrayList<>() ;
        return result;
    }
}
