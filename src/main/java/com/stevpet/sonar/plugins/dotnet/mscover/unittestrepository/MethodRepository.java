package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;

public interface MethodRepository {

    List<MethodId> getMethods(String fileId);

    void addMethod(String fileId, MethodId methodId);
    

}