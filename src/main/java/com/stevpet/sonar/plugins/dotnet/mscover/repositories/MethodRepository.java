package com.stevpet.sonar.plugins.dotnet.mscover.repositories;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIds;

public interface MethodRepository {

    /**
     * gets the methods matching the fileId
     * @param fileId
     * @return
     */
    MethodIds getMethods(String fileId);

    /**
     * add method and fileId
     * @param fileId - integer
     * @param methodId - method
     */
    void add(String fileId, MethodId methodId);

    /**
     * given a method, return the corresponding file
     * @param methodId
     * @return
     */
    String getFileId(MethodId methodId);
    

}
