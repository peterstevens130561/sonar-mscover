package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

public class BinConfigAssemblyResolver extends AssemblyResolverController {

        public void resolveAssembly(File projectDir,String assemblyName, String buildConfiguration) {
            String artifactPath = "bin/" + buildConfiguration + "/" + assemblyName;
            File assemblyFile=new File(projectDir, artifactPath);
            environment.setAssembly(assemblyFile);
        }
        
}
