package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import static org.junit.Assert.*;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIdModel;

public class MethodToSourceFileIdMapTest {
    private MethodToSourceFileIdMap map = new MethodToSourceFileIdMap();
    private String specFlowFileId = "1";
    private MethodIdModel specFlowMethodId;
    
    @Test
    public void getLongestContainedMethod_SpecFlowExamplesWithDigit_ShouldFind() {
        //Given coverage file has 
        String scenarioName="2Method";
        String scenarioNameInCoverage = scenarioName;
        String testName = "_" + scenarioNameInCoverage;
        
        
        findMethodInCoverageForTest(scenarioNameInCoverage, testName);
    }

    @Test
    public void getLongestContainedMethod_SpecFlowExamples_ShouldFind() {
        //Given coverage file has 
        String scenarioName = "2Method";
        String scenarioNameInCoverage=scenarioName;
        String testName=scenarioName + "variant3";
        
        findMethodInCoverageForTest(scenarioNameInCoverage, testName);
    }
    
    @Test
    public void getLongestContainedMethod_UnderscoreInMiddle_ShouldFind() {
        //Given coverage file has 
        String scenarioName = "Simple_Method";
        String scenarioNameInCoverage=scenarioName;
        String testName=scenarioName + "Simple_MethodFunn";
        
        findMethodInCoverageForTest(scenarioNameInCoverage, testName);
    }
    

    
    private void findMethodInCoverageForTest(String scenarioNameInCoverage,
            String testName) {
        specFlowMethodId = givenCoverageFileHasMethodWithFile(
                scenarioNameInCoverage, specFlowFileId);
        
        //When test is defined as

        specFlowMethodId.setMethodName(testName);
        
        thenNormalGetFails(specFlowMethodId);
        thenLongestMatchIsFound(scenarioNameInCoverage, specFlowFileId,
                specFlowMethodId);
    }

    private void thenNormalGetFails(MethodIdModel specFlowMethodId) {
        String gottenFile=map.get(specFlowMethodId);
        assertNull("normal get should fail",gottenFile);
    }
    
    private void thenLongestMatchIsFound(String scenarioNameInCoverage,
            String specFlowFileId, MethodIdModel specFlowMethodId) {
        String gottenFile;
        gottenFile=map.getLongestContainedMethod(specFlowMethodId);
        assertEquals("FileId should be found",specFlowFileId,gottenFile);
        //assertEquals("methodName resulting",scenarioNameInCoverage,specFlowMethodId.getMethodName());
    }
    
    private MethodIdModel givenCoverageFileHasMethodWithFile(
            String specFlowTestName, String specFlowFileId) {
        MethodIdModel specFlowMethodId = new MethodIdModel();
        specFlowMethodId.setClassName("features");
        specFlowMethodId.setModuleName("unittests.dll");
        specFlowMethodId.setNamespaceName("bhi.fun");
        specFlowMethodId.setMethodName(specFlowTestName);
        map.add(specFlowMethodId, specFlowFileId);
        return specFlowMethodId;
    }
}
