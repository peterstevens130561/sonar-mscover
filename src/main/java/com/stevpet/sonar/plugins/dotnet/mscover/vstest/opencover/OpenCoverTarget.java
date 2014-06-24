package com.stevpet.sonar.plugins.dotnet.mscover.vstest.opencover;

public interface OpenCoverTarget {
    /**
     * 
     * @return the absolute path to the executable
     */
    String getExecutable();
    /**
     * 
     * @return the combined arguments
     */
    String getArguments() ;
}
