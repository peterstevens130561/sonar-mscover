package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl.MethodIds;

public interface MethodRepository {

    MethodIds getMethods(String fileId);

    void addMethod(String fileId, MethodId methodId);
    

}
