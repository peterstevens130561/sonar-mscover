package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.MethodIds;

public class DefaultMethodIds implements MethodIds {

    private List<MethodId> methodIds = new ArrayList<>();
    
    @Override
    public void add(MethodId methodId) {
        methodIds.add(methodId);
    }

    
    public Stream<MethodId> stream() {
        return methodIds.stream();
    }
    
}
