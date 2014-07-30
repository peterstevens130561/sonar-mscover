package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class NoAssemblyDefinedMsCoverException extends MsCoverException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NoAssemblyDefinedMsCoverException(String buildConfiguration,
            String buildPlatform) {
        super("No assembly defined for " + buildConfiguration + "|" + buildPlatform);
    }

}
