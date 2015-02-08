package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.implementation;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;

/**
 * Intended to replace the dotnet fun, so is a minimalistic implementation
 * @author stevpet
 *
 */

public class SimpleMicrosoftWindowsEnvironment implements BatchExtension,MicrosoftWindowsEnvironment {

    Logger LOG = LoggerFactory.getLogger(SimpleMicrosoftWindowsEnvironment.class);
    private VisualStudioSolution solution=null;

    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IMicrosoftWindowsEnvironment#getCurrentSolution()
     */
    @Override
    public VisualStudioSolution getCurrentSolution() {
        return solution;
    }


    @Override
    public void setCurrentSolution(VisualStudioSolution currentSolution) {
        this.solution=currentSolution;
    }



    @Override
    public List<File> getUnitTestSourceFiles() {
        return getCurrentSolution().getUnitTestSourceFiles();
    }

    @Override
    public List<String> getAssemblies() {
        List<String> coveredAssemblyNames = new ArrayList<String>();
        for (VisualStudioProject visualProject : getCurrentSolution().getProjects()) {
            coveredAssemblyNames.add(visualProject.getAssemblyName());
        }
        return coveredAssemblyNames;
      }


    @Override
    public List<String> getArtifactNames() {
        return getCurrentSolution().getArtifactNames();
    }
}
