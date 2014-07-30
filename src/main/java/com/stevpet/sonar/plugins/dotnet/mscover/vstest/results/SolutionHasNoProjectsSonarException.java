package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class SolutionHasNoProjectsSonarException extends MsCoverException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SolutionHasNoProjectsSonarException() {
        super("Solution has no projects");
    }

}
