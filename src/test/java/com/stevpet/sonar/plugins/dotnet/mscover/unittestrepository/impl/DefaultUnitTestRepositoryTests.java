package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.DefaultUnitTest;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIds;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTest;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.impl.DefaultMethodIds;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.UnitTestRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl.DefaultUnitTestRepository;

public class DefaultUnitTestRepositoryTests {
    private UnitTestRepository repository = new DefaultUnitTestRepository() ;
    
    @Test
    public void repositoryWithOneTests() {
        setupOneUnitTest();
        MethodIds methods = new DefaultMethodIds();
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
