package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MsCoverPropertiesMock;

public class IgnoreMissingAssemblyResolverTest {

    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private AssemblyResolver assemblyResolver = new IgnoreMissingAssemblyResolver();
    private File assemblyFile;
    private File resultFile;
    private String fileName="unittest.dll";
    Collection<String> list;
    
    @Before
    public void before() {
        assemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());

        givenAssembly(fileName);
    }
    
    @Test
    public void resolveAssembly_PropertyNotSet_ExpectNotResolved() {      
        resolveAssembly();
        verifyNotResolved();
    }
  
    @Test
    public void resolveAssembly_PropertyDoesNotContain_ExpectNotResolved() {
        givenAssemblyThatCanBeIgnored("different");

        resolveAssembly();       
        verifyNotResolved();    
    }
    
    @Test
    public void resolveAssembly_PropertyDoesContain_ExpectNotResolved() {

        givenAssemblyThatCanBeIgnored("different");
        givenAssemblyThatCanBeIgnored(fileName); // the match
        
        resolveAssembly();
        verifyShouldBeIgnored();       
    }

    private void givenAssemblyThatCanBeIgnored(String name) {
        if(list==null) {
            list=new ArrayList<String>();
        }
        list.add(name);
    }
    
    private void givenAssembly(String fileName) {
        assemblyFile = new File(fileName);
    }
    
    private void resolveAssembly() {
        msCoverPropertiesMock.givenUnitTestAssembliesThatCanBeIgnoredIfMissing(list);
        resultFile=assemblyResolver.resolveAssembly(assemblyFile, null, null);
    }
    
    private void verifyShouldBeIgnored() {
        assertNull(resultFile);
    }
    
    private void verifyNotResolved() {
        assertEquals(assemblyFile,resultFile);
    }
}
