package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.implementation;

import java.io.File;
import java.util.List;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;

public class SimpleVisualStudioSolution implements VisualStudioSolution {

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IVisualStudioSolution#getSolutionDir()
     */
    @Override
    public File getSolutionDir() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IVisualStudioSolution#getProjects()
     */
    @Override
    public List<VisualStudioProject> getProjects() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IVisualStudioSolution#getUnitTestProjects()
     */
    @Override
    public List<VisualStudioProject> getUnitTestProjects() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IVisualStudioSolution#getProjectFromSonarProject(org.sonar.api.resources.Project)
     */
    @Override
    public VisualStudioProject getProjectFromSonarProject(Project project) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IVisualStudioSolution#getProject(java.io.File)
     */
    @Override
    public VisualStudioProject getProject(File file) {
        // TODO Auto-generated method stub
        return null;
    }

}
