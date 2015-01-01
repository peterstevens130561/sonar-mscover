package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.Environment;

public class AssemblyResolverTestUtils {

    private AssemblyResolver assemblyResolver;
    private File resultFile;
    private File projectDir;
    private String assemblyName;
    private String buildConfiguration;
    private Environment environment = new Environment();

    /**
     * the resolver under test (mandatory
     * @param assemblyResolver
     */
    public void setAssemblyResolver(AssemblyResolver assemblyResolver) {

        this.assemblyResolver=assemblyResolver;
        assemblyResolver.setEnvironment(environment);
    }
    

    public  void givenAssembly(String assemblyName) {
        this.assemblyName = assemblyName;
    }

    public void givenProjectDir(File projectDir) {
        this.projectDir = projectDir;
    }
    public void resolveAssembly() {
        assemblyResolver.resolveAssembly(projectDir, assemblyName, buildConfiguration);
        resultFile=environment.getAssembly();
        
    }

    public void verifyShouldBeIgnored() {
        assertNull(resultFile);
    }
    
    public void verifyNotResolved() {
        assertEquals(projectDir,resultFile);
    }

    public void verifyResolvedAs(String string) {

        assertNotNull(resultFile);
        assertEquals(string.replaceAll("/","\\\\") ,resultFile.getPath());    
    }

    public void setBuildConfiguration(String buildConfiguration) {
        this.buildConfiguration=buildConfiguration;
    }


}