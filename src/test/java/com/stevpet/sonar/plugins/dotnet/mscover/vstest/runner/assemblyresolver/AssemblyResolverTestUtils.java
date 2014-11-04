package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

public class AssemblyResolverTestUtils {

    private AssemblyResolver assemblyResolver;
    private File resultFile;
    private File assemblyFile;
    private String buildConfiguration;
    private VisualStudioProject visualStudioProject;

    /**
     * the resolver under test (mandatory
     * @param assemblyResolver
     */
    public void setAssemblyResolver(AssemblyResolver assemblyResolver) {

        this.assemblyResolver=assemblyResolver;
    }
    
    /**
     * The project (optional, depending on resolver)
     * @param mock
     */
    public void setVisualStudioProject(VisualStudioProject mock) {
        visualStudioProject = mock;
        
    }

    public  void givenAssembly(String fileName) {
        if(fileName!=null) {
            assemblyFile = new File(fileName);
        }
    }

    
    public void resolveAssembly() {
        resultFile=assemblyResolver.resolveAssembly(assemblyFile, visualStudioProject, buildConfiguration);
    }

    public void verifyShouldBeIgnored() {
        assertNull(resultFile);
    }
    
    public void verifyNotResolved() {
        assertEquals(assemblyFile,resultFile);
    }

    public void verifyResolvedAs(String string) {
        assertNotNull(resultFile);
        assertEquals(string.replaceAll("/","\\\\") ,resultFile.getPath());    
    }

    public void setBuildConfiguration(String buildConfiguration) {
        this.buildConfiguration=buildConfiguration;
    }


}