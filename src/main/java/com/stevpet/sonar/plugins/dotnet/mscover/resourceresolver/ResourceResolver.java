package com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver;

import org.sonar.api.BatchExtension;
import org.sonar.api.resources.File;

public interface ResourceResolver extends BatchExtension {

    /**
     * 
     * @param file
     *            somewhere in the project
     * @return the matching resource. If not in the project null is returned!
     */
    File getFile(java.io.File file);

}