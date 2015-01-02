package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public abstract class AssemblyResolverController implements AssemblyResolver {
    private Logger LOG = LoggerFactory.getLogger(AssemblyResolverController.class);
    private AssemblyResolver assemblyResolver ;
    private MsCoverProperties msCoverProperties;

    public File resolveChain(File assemblyFile,VisualStudioProject project, String buildConfiguration) {
        LOG.debug("trying");
        File file=resolveAssembly(assemblyFile,project,buildConfiguration) ;
        if(file == null) {
            LOG.debug("Ignoring");
            return null;
        }
        if( file.exists()) {
            LOG.debug("Found {}",file.getAbsoluteFile());
            return file;
        }
        LOG.debug("Not found {}",file.getAbsolutePath());
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
