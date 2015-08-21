package com.stevpet.sonar.plugings.dotnet.resharper.exceptions;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class InspectCodeException extends MsCoverException {

    private static final long serialVersionUID = 1L;

    public InspectCodeException(String msg, Exception inner) {
        super(msg,inner);
    }
}
