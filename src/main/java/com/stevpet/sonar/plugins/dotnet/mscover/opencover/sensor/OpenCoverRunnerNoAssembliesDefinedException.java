package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;


public class OpenCoverRunnerNoAssembliesDefinedException extends
        MsCoverException {

    /**
     * 
     */
    private static final long serialVersionUID = -8351538929141463241L;

    public OpenCoverRunnerNoAssembliesDefinedException() {
        super("Project has no assemblies");
    }

}
