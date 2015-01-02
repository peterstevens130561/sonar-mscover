package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;

public class BinConfigAssemblyResolver extends AssemblyResolverController {

        public File resolveAssembly(File assembly,VisualStudioProject project, String buildConfiguration) {
            File artifactDirectory=project.getDirectory();
            String artifactPath = "bin/" + buildConfiguration + "/" + project.getArtifactName();
            return new File(artifactDirectory, artifactPath);
        }
        
}
