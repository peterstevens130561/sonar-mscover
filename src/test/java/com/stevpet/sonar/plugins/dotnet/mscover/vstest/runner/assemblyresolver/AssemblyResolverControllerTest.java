package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;

import static org.mockito.Mockito.verify;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;

public class AssemblyResolverControllerTest {
    @Test
    public void test() {
        MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
        
        AssemblyResolver assemblyResolver = new ConcreteAssemblyResolverController() ;
        assemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());

        File assemblyFile = null;
        VisualStudioProject project = null;
        String buildConfiguration=null;
        File result=assemblyResolver.resolveChain(assemblyFile, project, buildConfiguration);
        assertNull(result);
    }

    @Test
    public void nonExisting() {
        MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
        
        AssemblyResolver assemblyResolver = new ConcreteAssemblyResolverController() ;
        assemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());
        AssemblyResolverMock assemblyResolverMock = new AssemblyResolverMock();
        AssemblyResolver nextResolver = assemblyResolverMock.getMock();
        assemblyResolver.setResolver(nextResolver);
        File assemblyFile = new File("willnotexist");
        VisualStudioProject project = null;
        String buildConfiguration=null;
        File result=assemblyResolver.resolveChain(assemblyFile, project, buildConfiguration);
        assertNull(result);
        verify(nextResolver).resolveChain(assemblyFile, project, buildConfiguration);
    }
    private class ConcreteAssemblyResolverController extends AssemblyResolverController {

        public File resolveAssembly(File assemblyFile,
                VisualStudioProject project, String buildConfiguration) {
            // TODO Auto-generated method stub
            return assemblyFile;
        }

        
    }
}
