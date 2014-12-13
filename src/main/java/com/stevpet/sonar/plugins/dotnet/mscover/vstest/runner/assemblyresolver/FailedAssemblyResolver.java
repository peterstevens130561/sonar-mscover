package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.exceptions.MsCoverUnitTestAssemblyDoesNotExistException;

public class FailedAssemblyResolver extends AssemblyResolverController {

    public File resolveAssembly(File assemblyFile, VisualStudioProject project,
            String buildConfiguration) {
        throw new MsCoverUnitTestAssemblyDoesNotExistException(assemblyFile);
    }

}