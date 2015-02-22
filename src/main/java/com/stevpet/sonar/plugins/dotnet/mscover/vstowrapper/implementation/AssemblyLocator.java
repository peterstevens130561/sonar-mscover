package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.implementation;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;

public interface AssemblyLocator {

    File locateAssembly(String projectName, File projectFile,
            VisualStudioProject project);

}