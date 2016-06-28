package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;



interface CoverageModuleSaver  {
    
    void save(File coverageRootDir, String testProjectName, String xml);

}
