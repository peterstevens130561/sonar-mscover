package com.stevpet.sonar.plugins.dotnet.resharper;

import org.sonar.api.BatchExtension;
import org.sonar.api.resources.Project;

public interface ResharperWorkflow extends BatchExtension {

    /**
     * Should perform what is needed to get the resharper issues into the current project. For the
     * first project that will mean that it has to run inspectcode.
     */
    void execute();

    /**
     * Should perform what is needed to get the resharper issues into the current module (childProject). For the
     * first module that will mean that it has to run inspectcode.
     * @param module
     */
    void executeModule(Project module);

}