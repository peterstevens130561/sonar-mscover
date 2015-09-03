package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.VisualStudioConfiguration;

public class OutputPathAssemblyResolver extends AssemblyResolverController {  
    
    private VisualStudioConfiguration visualStudioConfiguration ;
    public File resolveAssembly(File assemblyFile, VisualStudioProject project,
            String buildConfiguration) {
            String[]  hintPaths=visualStudioConfiguration.getOutputPaths();
            if(hintPaths==null || hintPaths.length==0) {
                return assemblyFile;
            }
            String artifact=project.getArtifactName();
            for(String hintPath: hintPaths) {
                File file= new File(hintPath,artifact);  
                if(file.exists()) {
                    return file;
                }
            }

            return assemblyFile;
    }



}