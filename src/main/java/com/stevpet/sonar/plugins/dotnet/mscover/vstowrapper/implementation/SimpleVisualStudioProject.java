package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.implementation;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;

public class SimpleVisualStudioProject implements VisualStudioProject {

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IVisualStudioProject#getAssemblyName()
     */
    @Override
    public String getAssemblyName() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IVisualStudioProject#getArtifact(java.lang.String, java.lang.String)
     */
    @Override
    public File getArtifact(String buildConfiguration, String buildPlatform) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IVisualStudioProject#isUnitTest()
     */
    @Override
    public boolean isUnitTest() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IVisualStudioProject#getArtifactName()
     */
    @Override
    public String getArtifactName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IVisualStudioProject#getDirectory()
     */
    @Override
    public File getDirectory() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IVisualStudioProject#getName()
     */
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IVisualStudioProject#isTest()
     */
    @Override
    public boolean isTest() {
        // TODO Auto-generated method stub
        return false;
    }

}
