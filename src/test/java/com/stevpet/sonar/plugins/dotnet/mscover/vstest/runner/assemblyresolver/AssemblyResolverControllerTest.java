package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Test;

import static org.mockito.Mockito.verify;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.Environment;

public class AssemblyResolverControllerTest {
    @Test
    public void test() {
        MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
        
        AssemblyResolver assemblyResolver = new ConcreteAssemblyResolverController() ;
        Environment finderResult = new Environment();
        assemblyResolver.setEnvironment(finderResult);
        assemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());
        AssemblyResolverMock assemblyResolverMock = new AssemblyResolverMock();
        AssemblyResolver nextResolver = assemblyResolverMock.getMock();
        //assemblyResolver.setResolver(nextResolver);
        File assemblyFile = null;
        String assemblyName=null;
        String buildConfiguration=null;
        assemblyResolver.resolveChain(assemblyFile, assemblyName, buildConfiguration);

        assertFalse(finderResult.exists());
    }

    @Test
    public void nonExisting() {
        MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
        
        AssemblyResolver assemblyResolver = new ConcreteAssemblyResolverController() ;
        Environment finderResult = new Environment();
        assemblyResolver.setEnvironment(finderResult);
        
        assemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());
        AssemblyResolverMock assemblyResolverMock = new AssemblyResolverMock();
        AssemblyResolver nextResolver = assemblyResolverMock.getMock();
        assemblyResolver.setResolver(nextResolver);
        File assemblyFile = new File("willnotexist");
        String assemblyName="willnotexist";
        String buildConfiguration=null;
        assemblyResolver.resolveChain(assemblyFile, assemblyName, buildConfiguration);
        assertFalse(finderResult.exists());
        
        verify(nextResolver).resolveChain(assemblyFile, assemblyName, buildConfiguration);
    }
    
    private class ConcreteAssemblyResolverController extends AssemblyResolverController {

        public void resolveAssembly(File assemblyFile,
                String  assemblyName, String buildConfiguration) {

        }


        
    }
}
