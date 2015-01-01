package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.Environment;

public abstract class AssemblyResolverController implements AssemblyResolver {
    private Logger LOG = LoggerFactory.getLogger(AssemblyResolverController.class);
    private AssemblyResolver assemblyResolver ;
    private MsCoverProperties msCoverProperties;
    protected Environment environment;

    public void resolveChain(File projectDir,String assemblyName, String buildConfiguration) {
        LOG.debug("trying");
        resolveAssembly(projectDir,assemblyName,buildConfiguration) ;
        if(environment.isIgnore()) {
            LOG.debug("Ignoring");
            return;
        }
        if( environment.isCheck() && environment.exists()) {
                LOG.debug("Found {}",environment.getAssembly().getAbsolutePath());
            return ;
        }
        LOG.debug("Not found {}",environment.getAssenblyName());
        if(assemblyResolver==null) {
            LOG.warn( "Hit end of resolverChain without finding solution");
            return;
        }
        assemblyResolver.resolveChain(projectDir,assemblyName,buildConfiguration);

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
    
    public void setEnvironment(Environment finderResult) {
        this.environment=finderResult;
    }

}
