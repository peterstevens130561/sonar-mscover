package com.stevpet.sonar.plugins.dotnet.mscover.exceptions;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class MsCoverUnitTestAssemblyDoesNotExistException extends
        MsCoverException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MsCoverUnitTestAssemblyDoesNotExistException(File artifactFile) {
        super("Unit test assembly does not exist " + (artifactFile==null?"(null)":artifactFile.getAbsolutePath()));
    }

}
