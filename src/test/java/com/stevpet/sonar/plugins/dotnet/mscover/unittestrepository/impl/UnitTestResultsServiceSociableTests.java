package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.List;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.DefaultUnitTest;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTest;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.MethodRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.SourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.UnitTestRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl.DefaultMethodRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl.DefaultSourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl.DefaultUnitTestRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.UnitTestResultService;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl.DefaultUnitTestResultService;

public class UnitTestResultsServiceSociableTests implements UnitTestResultsServiceTests {

    private String filePath;
    private MethodRepository methodRepository = new DefaultMethodRepository();
    private SourceFileRepository sourceFileRepository = new DefaultSourceFileRepository();
    private UnitTestRepository unitTestRepository = new DefaultUnitTestRepository();
    UnitTestResultService service = new DefaultUnitTestResultService(methodRepository, sourceFileRepository, unitTestRepository);
    
            @Test
            public void emptyRepositoriesExpectEmptyList() {
                filePath="booh";
                List<UnitTest> list = service.getUnitTestsFor(filePath);
                assertNotNull(list);
                assertNotNull("should have valid list",list);
            }
            
            @Test
            public void simpleOne() {
                sourceFileRepository.add("1", "booh");
                setupOneMethod();
                setupOneUnitTest();
                List<UnitTest> result = service.getUnitTestsFor("booh");
                assertNotNull("should have valid list",result);
                assertEquals(1,result.size());
                assertEquals("firsttest",result.get(0).getTestName());
            }
            
            @Test
            public void unknownSourceFile() {
                setupOneSourceFile();
                setupOneMethod();
                
                List<UnitTest> result = service.getUnitTestsFor("booh");
                assertNotNull("should have valid list",result);
                assertEquals(0,result.size());
            }


            @Test
            @Override
            public void sourceFileHasSomeTwoMethodsAndOneMethodHasTwoTestsShouldHaveThreeTests() {
                setupOneSourceFile();   
                setupTwoMethods();
                setupOneUnitTest();
                setupTwoTestsForOneMethod();
                
                List<UnitTest> result = service.getUnitTestsFor("bogus");
                
                assertEquals(3,result.size());
            }
            
            private void setupOneSourceFile() {
                sourceFileRepository.add("1", "bogus");
            }
            
            private void setupOneMethod() {
                MethodId methodId = new MethodId("module.dll","namespace","class","method1");
                methodRepository.add("1",methodId );
            }
            
            private void setupTwoMethods() {
                setupOneMethod();
                setupMethodTwo();
            }

            private void setupMethodTwo() {
                MethodId methodId = new MethodId("module.dll","namespace","class","method2");
                methodRepository.add("1",methodId );
            }
            
            private void setupOneUnitTest() {
                UnitTestMethodResult unitTestResult = new UnitTestMethodResult();
                unitTestResult.setNamespaceNameFromClassName("namespace.class").setClassName("class").setTestName("method1").setModuleFromCodeBase("a:\\module.dll");
                UnitTest unitTest = new DefaultUnitTest("firsttest", unitTestResult);
                unitTestRepository.addUnitTest(unitTest);
            }
            
            private void setupTwoTestsForOneMethod() {
                UnitTestMethodResult unitTestResult = new UnitTestMethodResult();
                unitTestResult.setNamespaceNameFromClassName("namespace.class").setClassName("class").setTestName("method2").setModuleFromCodeBase("a:\\module.dll");
                UnitTest unitTest = new DefaultUnitTest("test2 row1", unitTestResult);
                unitTestRepository.addUnitTest(unitTest);
                unitTestResult = new UnitTestMethodResult();
                unitTestResult.setNamespaceNameFromClassName("namespace.class").setClassName("class").setTestName("method2").setModuleFromCodeBase("a:\\module.dll");
                unitTest = new DefaultUnitTest("test2 row2", unitTestResult);
                unitTestRepository.addUnitTest(unitTest);
            }
            

}
