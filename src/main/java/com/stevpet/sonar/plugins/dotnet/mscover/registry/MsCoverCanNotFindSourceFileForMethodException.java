package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;


public class MsCoverCanNotFindSourceFileForMethodException extends
        MsCoverException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MsCoverCanNotFindSourceFileForMethodException(String msg) {
        super(msg);
    }

}
