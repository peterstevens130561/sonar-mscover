package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import static org.junit.Assert.*;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileId;
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

    @Test
    public void getLongestContainedMethod_SpecFlowExamples_ShouldFind() {
        //Given coverage file has 
        String scenarioName = "2Method";
        String scenarioNameInCoverage=scenarioName;
        String testName=scenarioName + "variant3";
        
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

    private void thenNormalGetFails(MethodId specFlowMethodId) {
        FileId gottenFile=map.getFileId(specFlowMethodId);
        assertNull("normal get should fail",gottenFile.getId());
    }
    
    private void thenLongestMatchIsFound(String scenarioNameInCoverage,
            String specFlowFileId, MethodId specFlowMethodId) {
        String gottenFile;
        gottenFile=map.getLongestContainedMethod(specFlowMethodId);
        assertEquals("FileId should be found",specFlowFileId,gottenFile);
        assertEquals("methodName resulting",scenarioNameInCoverage,specFlowMethodId.getMethodName());
    }
    
    private MethodId givenCoverageFileHasMethodWithFile(
            String specFlowTestName, String specFlowFileId) {
        MethodId specFlowMethodId = new MethodId();
        specFlowMethodId.setClassName("features");
        specFlowMethodId.setModuleName("unittests.dll");
        specFlowMethodId.setNamespaceName("bhi.fun");
        specFlowMethodId.setMethodName(specFlowTestName);
        map.add(specFlowMethodId, specFlowFileId);
        return specFlowMethodId;
    }
}
