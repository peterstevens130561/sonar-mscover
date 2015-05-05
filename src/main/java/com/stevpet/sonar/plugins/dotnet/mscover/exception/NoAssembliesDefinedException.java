package com.stevpet.sonar.plugins.dotnet.mscover.exception;




public class NoAssembliesDefinedException extends
        MsCoverException {

    /**
     * 
     */
    private static final long serialVersionUID = -8351538929141463241L;

    public NoAssembliesDefinedException() {
        super("Project has no assemblies");
    }

}
