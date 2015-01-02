package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.AssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.BaseAssemblyResolver;

public class BaseAssembliesFinder extends AbstractAssembliesFinder implements AssemblyResolver{

    AssemblyResolver assemblyResolver = new BaseAssemblyResolver();
    public BaseAssembliesFinder(MsCoverProperties propertiesHelper) {
        super(propertiesHelper);
    }

    public File resolveAssembly(File assemblyFile, VisualStudioProject project,
            String buildConfiguration) {

        return null;
    }

    /**
     * Invoked from AbstractAssembliesFinder
     */
    public File searchNonExistingFile(File assemblyFile,
            VisualStudioProject project, String buildConfiguration) {

        return resolveChain(assemblyFile,project,buildConfiguration);
    }

    public File resolveChain(File assemblyFile, VisualStudioProject project,
            String buildConfiguration) {

        return assemblyResolver.resolveChain(assemblyFile, project, buildConfiguration);
    }

    public void setResolver(AssemblyResolver assemblyResolver) {
        this.assemblyResolver.setResolver(assemblyResolver);
    }

    public void setMsCoverProperties(MsCoverProperties msCoverProperties) {
        assemblyResolver.setMsCoverProperties(msCoverProperties);
    }

    public MsCoverProperties getMsCoverProperties() {
        return assemblyResolver.getMsCoverProperties();
    }




}
