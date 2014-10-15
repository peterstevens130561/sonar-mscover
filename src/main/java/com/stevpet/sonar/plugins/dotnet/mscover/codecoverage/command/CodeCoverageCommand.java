package com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command;

public interface CodeCoverageCommand {

    public abstract void setCoveragePath(String path);

    public abstract void setOutputPath(String path);

    /**
     * 
     * 
     * set the path to the .sonar folder
     * 
     * @param path
     */
    public abstract void setSonarPath(String path);

    /**
     * install the binary from resource by first removing a potential old one, then
     * taking it from the resource jar and put it in the sonar working directory
     */
    public abstract void install();

}