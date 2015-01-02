package com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command;

import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ShellCommand;

public interface CodeCoverageCommand extends ShellCommand{

    void setCoveragePath(String path);

    void setOutputPath(String path);

    /**
     * 
     * 
     * set the path to the .sonar folder
     * 
     * @param path
     */
    void setSonarPath(String path);

    /**
     * install the binary from resource by first removing a potential old one, then
     * taking it from the resource jar and put it in the sonar working directory
     */
    void install();

}