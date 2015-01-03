package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.implementation;

import java.io.File;

import org.sonar.api.BatchExtension;
import org.sonar.api.config.Settings;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;

/**
 * Intended to replace the dotnet fun, so is a minimalistic implementation
 * @author stevpet
 *
 */
public class SimpleMicrosoftWindowsEnvironment implements BatchExtension,MicrosoftWindowsEnvironment {

    private VisualStudioSolution solution=null;
    public SimpleMicrosoftWindowsEnvironment(Settings settings,ModuleFileSystem moduleFileSystem) {
        //File solutionFile = getSolutionFile(settings,moduleFileSystem.baseDir());
        //solution = new SimpleVisualStudioSolution(solutionFile);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IMicrosoftWindowsEnvironment#getCurrentSolution()
     */
    @Override
    public VisualStudioSolution getCurrentSolution() {
        // TODO Auto-generated method stub
        return solution;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IMicrosoftWindowsEnvironment#getWorkingDirectory()
     */
    @Override
    public String getWorkingDirectory() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IMicrosoftWindowsEnvironment#isTestExecutionDone()
     */
    @Override
    public boolean isTestExecutionDone() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IMicrosoftWindowsEnvironment#setTestExecutionDone()
     */
    @Override
    public void setTestExecutionDone() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IMicrosoftWindowsEnvironment#getCurrentProject(java.lang.String)
     */
    @Override
    public VisualStudioProject getCurrentProject(String projectName) {
        // TODO Auto-generated method stub
        return null;
    }

}
