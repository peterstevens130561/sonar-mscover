package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;



interface CoverageModuleSaver  {
    /**
     * The xml doc is stored in directory below coverageRootDir, name of directory is the module in the xml doc
     * and file is named after the testProjectName.
     * <coverageRootDir>/<module>/<testProjectName>.xml
     *
     * @param coverageRootDir - root
     * @param testProjectName - name of the file, use the name of the test project to make sure it is unique
     * @param xml - fully qualified xml doc
     */
    void save(File coverageRootDir, String testProjectName, String xml);

}
