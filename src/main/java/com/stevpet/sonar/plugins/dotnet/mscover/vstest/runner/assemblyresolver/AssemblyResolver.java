package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.Environment;

public interface AssemblyResolver  {

    void resolveChain(File assemblyFile, String assemblyName,
            String buildConfiguration);

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssemblyResolver#setFinder(com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder)
     */
    void setResolver(AssemblyResolver assemblyResolver);

    void setMsCoverProperties(MsCoverProperties msCoverProperties);

    MsCoverProperties getMsCoverProperties();
    
    /**
     * 
     * @param assemblyFile
     * @param project
     * @param buildConfiguration
     * @return  void
     * 
     * Use finderResult to save the result
     */
    void resolveAssembly(File assemblyFile,String assemblyName, String buildConfiguration);

    void setEnvironment(Environment environment);

}