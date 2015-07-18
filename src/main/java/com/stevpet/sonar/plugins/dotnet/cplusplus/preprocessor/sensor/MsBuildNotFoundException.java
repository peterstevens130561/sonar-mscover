package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import java.io.File;

import org.sonar.api.utils.SonarException;

public class MsBuildNotFoundException extends SonarException {

    public MsBuildNotFoundException(File msBuildFile) {
        super("msbuild.exe does not exist as " + msBuildFile.getAbsolutePath());
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
