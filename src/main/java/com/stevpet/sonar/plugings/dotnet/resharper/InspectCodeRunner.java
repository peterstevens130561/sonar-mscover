package com.stevpet.sonar.plugings.dotnet.resharper;

import java.io.File;
import java.util.Collection;

import org.sonar.api.BatchExtension;
import org.sonar.api.resources.Project;

public interface InspectCodeRunner extends BatchExtension {
    /**
     * Execute inspectCode
     * @param project
     * @return resharper report
     */
    Collection<File> inspectCode(Project project);
}