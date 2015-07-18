package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import org.sonar.api.utils.SonarException;

public class MsBuildDirNotFoundException extends SonarException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public MsBuildDirNotFoundException(String path) {
        super("Directory in which msbuild should be found does not exist: "+ path);
    }

}
