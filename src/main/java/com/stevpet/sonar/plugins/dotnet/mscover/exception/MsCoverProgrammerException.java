package com.stevpet.sonar.plugins.dotnet.mscover.exception;

public class MsCoverProgrammerException extends MsCoverException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MsCoverProgrammerException(String msg) {
        super("Exception caused by programmmer :" + msg);
    }

}
