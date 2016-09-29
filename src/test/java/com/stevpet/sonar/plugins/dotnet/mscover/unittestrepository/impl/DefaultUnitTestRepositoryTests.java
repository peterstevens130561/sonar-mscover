package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.DefaultUnitTest;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.UnitTest;

public class DefaultUnitTestRepositoryTests {
    private UnitTestRepository repository = new DefaultUnitTestRepository() ;
    
    @Test
    public void repositoryWithOneTests() {
        setupOneUnitTest();
        List<MethodId> methods = new ArrayList<>();
        methods.add(new MethodId("module.dll","namespace","class","method1"));
        methods.add(new MethodId("module.dll","namespace","class","method2"));
        List<UnitTest> list = repository.getUnitTests(methods);
        assertEquals(1,list.size());
    }
    
    private void setupOneUnitTest() {
        UnitTestMethodResult unitTestResult = new UnitTestMethodResult();
        unitTestResult.setNamespaceNameFromClassName("namespace.class").setClassName("class").setTestName("method1").setModuleFromCodeBase("a:\\module.dll");
        UnitTest unitTest = new DefaultUnitTest("firsttest", unitTestResult);
        repository.addUnitTest(unitTest);
    }
    
    private void setupTwoTestsForOneMethod() {
        UnitTestMethodResult unitTestResult = new UnitTestMethodResult();
        unitTestResult.setNamespaceNameFromClassName("namespace.class").setClassName("class").setTestName("method2").setModuleFromCodeBase("a:\\module.dll");
        UnitTest unitTest = new DefaultUnitTest("test2 row1", unitTestResult);
        repository.addUnitTest(unitTest);
        unitTestResult = new UnitTestMethodResult();
        unitTestResult.setNamespaceNameFromClassName("namespace.class").setClassName("class").setTestName("method2").setModuleFromCodeBase("a:\\module.dll");
        unitTest = new DefaultUnitTest("test2 row2", unitTestResult);
        repository.addUnitTest(unitTest);
    }
}
