package com.stevpet.sonar.plugins.dotnet.mscover.exception;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;


public class MsCoverRequiredPropertyMissingException extends MsCoverException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * throw exception to indicate that a required property is missing.
     * @param property
     */
    public MsCoverRequiredPropertyMissingException(String property) {
        super("Required property " + property + " missing");
    }

}
