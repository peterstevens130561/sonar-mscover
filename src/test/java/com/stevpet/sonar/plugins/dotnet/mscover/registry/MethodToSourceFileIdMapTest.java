package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import static org.junit.Assert.*;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;

public class MethodToSourceFileIdMapTest {
    private MethodToSourceFileIdMap map = new MethodToSourceFileIdMap();
    private String specFlowFileId = "1";
    private MethodId specFlowMethodId;
    
    @Test
    public void getLongestContainedMethod_SpecFlowExamplesWithDigit_ShouldFind() {
        //Given coverage file has 
        String scenarioName="2Method";
        String scenarioNameInCoverage = scenarioName;
        String testName = "_" + scenarioNameInCoverage;
        
        
        findMethodInCoverageForTest(scenarioNameInCoverage, testName);
    }

   
    
    private void findMethodInCoverageForTest(String scenarioNameInCoverage,
            String testName) {
        specFlowMethodId = givenCoverageFileHasMethodWithFile(
                scenarioNameInCoverage, specFlowFileId);
        
        //When test is defined as

        specFlowMethodId=new MethodId("unittests.dll","bhi.fun","bhi.fun",testName);
        
        thenNormalGetFails(specFlowMethodId);
    }

    private void thenNormalGetFails(MethodId specFlowMethodId) {
        String gottenFile=map.get(specFlowMethodId);
        assertNull("normal get should fail",gottenFile);
    }
    

    
    private MethodId givenCoverageFileHasMethodWithFile(
            String specFlowTestName, String specFlowFileId) {
        MethodId specFlowMethodId = new MethodId("unittests.dll","bhi.fun","bhi.fun",specFlowTestName);
        map.add(specFlowMethodId, specFlowFileId);
        return specFlowMethodId;
    }
}
