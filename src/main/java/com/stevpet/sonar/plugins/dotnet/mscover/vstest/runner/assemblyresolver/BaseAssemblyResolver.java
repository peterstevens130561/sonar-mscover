package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

public class BaseAssemblyResolver extends AssemblyResolverController {

    public File resolveAssembly(File assemblyFile, VisualStudioProject project,
            String buildConfiguration) {
        return assemblyFile;
    }


}
