package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.AssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.BaseAssemblyResolver;

public class BaseAssembliesFinder extends AbstractAssembliesFinder implements AssemblyResolver{

    AssemblyResolver assemblyResolver = new BaseAssemblyResolver();
    public BaseAssembliesFinder(MsCoverProperties propertiesHelper) {
        super(propertiesHelper);
    }

    public void resolveAssembly(File projectDir, String assemblyName,
            String buildConfiguration) {
    }

    /**
     * Invoked from AbstractAssembliesFinder
     */
    public void searchNonExistingFile(File projectDir,
            String assemblyName, String buildConfiguration) {

       resolveChain(projectDir,assemblyName,buildConfiguration);
    }

    public void resolveChain(File projectDir, String assemblyName,
            String buildConfiguration) {
        assemblyResolver.setEnvironment(getEnvironment());
        assemblyResolver.resolveChain(projectDir, assemblyName, buildConfiguration);
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
