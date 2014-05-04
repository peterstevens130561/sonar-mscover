package com.stevpet.sonar.plugins.dotnet.mscover.exception;

import java.io.IOException;

import org.sonar.api.utils.SonarException;

public class MsCoverException extends SonarException {
    private static final long serialVersionUID = -7658006430846367652L;

    public MsCoverException(String msg) {
       super(msg);
    }

    public MsCoverException(String msg, Exception e) {
        super(msg,e);
    }

}
