package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.exceptions.MsCoverUnitTestAssemblyDoesNotExistException;

public class FailedAssemblyResolverTest {
    @Test(expected=MsCoverUnitTestAssemblyDoesNotExistException.class)
    public void resolveAssembly_ThrowsMsCoverException() {
        FailedAssemblyResolver failedAssemblyResolver = new FailedAssemblyResolver();
        String fileName = "DummyTest.dll";
        File assemblyFile = new File(fileName);
        File result=failedAssemblyResolver.resolveAssembly(assemblyFile, null, null);
    }
}
