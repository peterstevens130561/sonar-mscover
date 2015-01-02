package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;

public class HintPathAssemblyResolver extends AssemblyResolverController
{  
    public File resolveAssembly(File assemblyFile, VisualStudioProject project,
            String buildConfiguration) {
            String hintPath=getMsCoverProperties().getUnitTestHintPath();
            if(StringUtils.isEmpty(hintPath)) {
                return assemblyFile;
            }
            String artifact=project.getArtifactName();
            return new File(hintPath,artifact);
    }


}
