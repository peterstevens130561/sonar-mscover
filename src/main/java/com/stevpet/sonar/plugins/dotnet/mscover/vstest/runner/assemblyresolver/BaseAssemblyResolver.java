package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;

public class BaseAssemblyResolver extends AssemblyResolverController {

    public File resolveAssembly(File assemblyFile, VisualStudioProject project,
            String buildConfiguration) {
        return assemblyFile;
    }


}
