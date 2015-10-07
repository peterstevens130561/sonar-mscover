package com.stevpet.sonar.plugins.dotnet.resharper.exceptions;

public class InspectCodeRunnerException extends InspectCodeException {
    private static final long serialVersionUID = 1L;

    public InspectCodeRunnerException(String msg, Exception inner) {
        super(msg,inner);
    }
}
