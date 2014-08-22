package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class MsCoverParserException extends MsCoverException {

    public MsCoverParserException(String msg, Exception e) {
        super(msg, e);
    }

}
