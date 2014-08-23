package com.stevpet.sonar.plugins.dotnet.mscover.parser.exceptions;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class ParserSubjectErrorException extends MsCoverException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ParserSubjectErrorException(File file) {
        super("One or more errors detected during parsing " + file.getAbsolutePath());
    }

}
