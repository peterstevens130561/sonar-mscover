package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.implementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

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
    public SimpleMicrosoftWindowsEnvironment(VisualStudioAssemblyLocator assemblyLocator, VisualStudioSolutionBuilder solutionBuilder) {

        solution=solutionBuilder.build(assemblyLocator);
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
