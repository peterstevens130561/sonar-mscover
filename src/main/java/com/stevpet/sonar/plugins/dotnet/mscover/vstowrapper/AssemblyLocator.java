package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper;

import java.io.File;

public interface AssemblyLocator {

    File locateAssembly(String projectName, File projectFile,
            VisualStudioProject project);

}