package com.stevpet.sonar.plugins.dotnet.mscover.vstest.exceptions;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class MsCoverInvalidSonarWorkingDir extends MsCoverException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MsCoverInvalidSonarWorkingDir(String msg) {
        super("not a directory under .sonar " + msg);
    }

}
