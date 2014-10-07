package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.exceptions.MsCoverUnitTestAssemblyDoesNotExistException;

class DefaultAssembliesFinder extends AbstractAssembliesFinder implements AssembliesFinder {

    protected DefaultAssembliesFinder(MsCoverProperties propertiesHelper) {
        super(propertiesHelper);
        this.propertiesHelper=propertiesHelper;
    }
    
    public void onNonExistingFile(File assemblyFile) {
        if(!assemblyFile.exists()) {
            throw new MsCoverUnitTestAssemblyDoesNotExistException(assemblyFile);
        }
    }

}
