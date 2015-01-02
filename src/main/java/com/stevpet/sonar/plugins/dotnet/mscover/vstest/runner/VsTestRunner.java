package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;

public interface VsTestRunner {

    public abstract void setSonarPath(String path);

    public abstract String getSonarPath();

    public abstract void setPropertiesHelper(MsCoverProperties propertiesHelper);

    /**
     * 
     * @param solution
     * @deprecated use {@link #setStartDir(File)}
     */
    @Deprecated
    public abstract void setSolution(VisualStudioSolution solution);

    public abstract void setSolutionDirectory(File solutionDirectory);

    public abstract File getSolutionDirectory();

    public abstract void setDoCodeCoverage(boolean doCodeCoverage);

    public abstract boolean shouldRun();

    /**
     * if mode is set to runvstest the runner will attempt to run test & obtain coverage
     * information. Make sure all properties are set:
     * -solutionDirectory
     * -outputPath
     * Result is the .cover file
     * @return
     */
    public abstract void runTests();

    public abstract VSTestCommand prepareTestCommand();

    /**
     * parse test log to get paths to result files
     */
    public abstract void getResultPaths();

    /**
     * gets the path to the .cover file
     * @return
     */
    public abstract String getCoveragePath();

    /**
     * sets the path to the .cover file
     * @param coveragePath
     */
    public abstract void setCoveragePath(String coveragePath);

    public abstract String getResultsXmlPath();

    public abstract void setResultsPath(String resultsPath);

    public abstract String getCoverageXmlPath();

    public abstract void setCoverageXmlPath(String outputPath);

    /**
     * Remove the files generated by VsTest. Requires that the sonarPath is set, and that it ends with .sonar
     */
    public abstract void clean();

    /**
     * Use to set the directory from where to start searching for the projects
     * @param startDir
     */
    public abstract void setStartDir(File startDir);

}