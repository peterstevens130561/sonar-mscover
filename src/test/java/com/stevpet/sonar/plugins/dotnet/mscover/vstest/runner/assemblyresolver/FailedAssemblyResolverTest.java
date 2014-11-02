package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.exceptions.MsCoverUnitTestAssemblyDoesNotExistException;

public class FailedAssemblyResolverTest {
    private AssemblyResolverTestUtils utils = new AssemblyResolverTestUtils();
    private FailedAssemblyResolver assemblyResolver = new FailedAssemblyResolver();
    private String fileName="unittest.dll";
    
    @Test(expected=MsCoverUnitTestAssemblyDoesNotExistException.class)   
    public void resolveAssembly_ThrowsMsCoverException() {
        utils.setAssemblyResolver(assemblyResolver);
        utils.givenAssembly(fileName);
        utils.resolveAssembly();
    }
}
