package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;

public class IgnoreMissingAssemblyResolverTest {

    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private AssemblyResolver assemblyResolver = new IgnoreMissingAssemblyResolver();
    private AssemblyResolverTestUtils utils = new AssemblyResolverTestUtils();

    private String fileName="unittest.dll";
    Collection<String> list;
    
    @Before
    public void before() {
        assemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());
        utils.setAssemblyResolver(assemblyResolver);
        utils.givenAssembly(fileName);
        msCoverPropertiesMock.givenUnitTestAssembliesThatCanBeIgnoredIfMissing(null);
    }
    
    @Test
    public void resolveAssembly_PropertyNotSet_ExpectNotResolved() {      
        utils.resolveAssembly();
        utils.verifyNotResolved();
    }
  
    @Test
    public void resolveAssembly_PropertyDoesNotContain_ExpectNotResolved() {
        givenAssemblyThatCanBeIgnored("different");

        utils.resolveAssembly();       
        utils.verifyNotResolved();    
    }
    
    @Test
    public void resolveAssembly_PropertyDoesContain_ExpectNotResolved() {

        givenAssemblyThatCanBeIgnored("different");
        givenAssemblyThatCanBeIgnored(fileName); // the match
        
        utils.resolveAssembly();
        utils.verifyShouldBeIgnored();       
    }

    private void givenAssemblyThatCanBeIgnored(String name) {
        if(list==null) {
            list=new ArrayList<String>();
        }
        list.add(name);
        msCoverPropertiesMock.givenUnitTestAssembliesThatCanBeIgnoredIfMissing(list);
    }
    
    

}
