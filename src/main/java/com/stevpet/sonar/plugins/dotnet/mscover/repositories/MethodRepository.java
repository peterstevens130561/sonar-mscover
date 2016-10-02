package com.stevpet.sonar.plugins.dotnet.mscover.repositories;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIds;

public interface MethodRepository {

    MethodIds getMethods(String fileId);

    void addMethod(String fileId, MethodId methodId);
    

}
