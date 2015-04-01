package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.AssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.BinConfigAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.FailedAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.HintPathAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.IgnoreMissingAssemblyResolver;

public class DefaultAssembliesFinder extends BaseAssembliesFinder {

    public DefaultAssembliesFinder(MsCoverProperties propertiesHelper) {
        super(propertiesHelper);
            AssemblyResolver[] assembliesFinders = {
                    new FailedAssemblyResolver(),
                    new IgnoreMissingAssemblyResolver(),
                    new HintPathAssemblyResolver(),
                    new BinConfigAssemblyResolver()
            };

            AssemblyResolver nextResolver=null;
             for(AssemblyResolver resolver: assembliesFinders) {
                 resolver.setMsCoverProperties(propertiesHelper);
                 resolver.setResolver(nextResolver);
                 nextResolver=resolver;
             }
             
             setMsCoverProperties(propertiesHelper);
             setResolver(nextResolver);
         }
    }

