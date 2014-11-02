package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;
import java.util.List;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public abstract class AssemblyResolverController implements AssemblyResolver {

    private AssemblyResolver assemblyResolver ;
    private MsCoverProperties msCoverProperties;

    public File resolveChain(File assemblyFile,VisualStudioProject project, String buildConfiguration) {
        File file=resolveAssembly(assemblyFile,project,buildConfiguration) ;
        if(file == null || file.exists()) {
            return file;
        }
        return assemblyResolver.resolveChain(assemblyFile,project,buildConfiguration);
    }


    public void setResolver(AssemblyResolver assemblyResolver) {
        this.assemblyResolver=assemblyResolver;
    }
    

    public void setMsCoverProperties(MsCoverProperties msCoverProperties) {
        this.msCoverProperties=msCoverProperties;
    }
    

    public MsCoverProperties getMsCoverProperties() {
        return msCoverProperties;
    }

}
