package com.stevpet.sonar.plugins.dotnet.mscover.exception;


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
