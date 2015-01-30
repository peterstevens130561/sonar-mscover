package com.stevpet.sonar.plugins.dotnet.mscover.parser.exceptions;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class MsCoverParserException extends MsCoverException {

    private static final long serialVersionUID = 1L;

    public MsCoverParserException(String msg, Exception e) {
        super(msg, e);
    }

}
