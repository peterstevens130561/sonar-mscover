package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.exceptions.MsCoverUnitTestAssemblyDoesNotExistException;

class DefaultAssembliesFinder extends AbstractAssembliesFinder implements AssembliesFinder {

    
    protected DefaultAssembliesFinder(MsCoverProperties propertiesHelper) {
        super(propertiesHelper);
        this.propertiesHelper=propertiesHelper;
    }
    
    public File searchNonExistingFile(File assemblyFile,
            VisualStudioProject project, String buildConfiguration) {
        if(!assemblyFile.exists()) {
            throw new MsCoverUnitTestAssemblyDoesNotExistException(assemblyFile);
        }
        return assemblyFile;
    }

}
