package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;


public interface AssemblyResolver  {

    File resolveChain(File assemblyFile, VisualStudioProject project,
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
     * @return :
     * - null if resolver concludes that assembly can not be resolved, stop the chain. 
     * - existing file will stop the chain
     * - return non-existing file to continue
     */
    File resolveAssembly(File assemblyFile,VisualStudioProject project, String buildConfiguration);

}