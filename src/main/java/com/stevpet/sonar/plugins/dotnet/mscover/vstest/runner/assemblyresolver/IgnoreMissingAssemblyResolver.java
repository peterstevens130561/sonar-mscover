package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.exceptions.MsCoverUnitTestAssemblyDoesNotExistException;

public class IgnoreMissingAssemblyResolver extends AssemblyResolverController {

    private Logger Log = LoggerFactory.getLogger(IgnoreMissingAssemblyResolver.class);
  
  
    public File resolveAssembly(File assemblyFile, VisualStudioProject project,
            String buildConfiguration) {

        Collection<String> canBeIgnoredIfMissing = getMsCoverProperties().getUnitTestAssembliesThatCanBeIgnoredIfMissing();
        if(canBeIgnoredIfMissing ==null) {
            return assemblyFile;
        }
        
        String assemblyName = assemblyFile.getName();
        if(canBeIgnoredIfMissing.contains(assemblyName)) {
            Log.warn("Ignoring non-existent unit test assembly {}",assemblyName);
            return null;
        }
        return assemblyFile;
    }

}
