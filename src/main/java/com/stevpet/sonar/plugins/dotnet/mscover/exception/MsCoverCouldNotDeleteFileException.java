package com.stevpet.sonar.plugins.dotnet.mscover.exception;

import java.io.File;

/**
 * Specialization class to use when deleting of a file failed
 */
public class MsCoverCouldNotDeleteFileException extends MsCoverException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MsCoverCouldNotDeleteFileException(File file,Exception e) {
        super("Could not delete file " + (file==null?"(null)":file.getAbsolutePath()) ,e);
    }

}
