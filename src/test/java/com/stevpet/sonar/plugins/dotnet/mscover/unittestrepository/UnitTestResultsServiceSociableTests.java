package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl.DefaultMethodRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl.DefaultSourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl.DefaultUnitTestRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl.UnitTestRepository;

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
                sourceFileRepository.addFile("1", "booh");
                setupOneMethod();                
                List<UnitTest> result = service.getUnitTestsFor("booh");
                assertNotNull("should have valid list",result);
                assertEquals(1,result.size());
            }
            
            @Test
            public void unknownSourceFile() {
                setupOneSourceFile();
                setupOneMethod();
                
                List<UnitTest> result = service.getUnitTestsFor("booh");
                assertNotNull("should have valid list",result);
                assertEquals(0,result.size());
            }



            @Override
            public void sourceFileHasSomeTwoMethodsAndOneMethodHasTwoTestsShouldHaveThreeTests() {
                setupOneSourceFile();   
                setupTwoMethods();
                setupUnitTests();
                
            }
            
            private void setupOneSourceFile() {
                sourceFileRepository.addFile("1", "bogus");
            }
            
            private void setupOneMethod() {
                MethodId methodId = new MethodId("module.dll","namespace","class","method1");
                methodRepository.addMethod("1",methodId );
            }
            
            private void setupTwoMethods() {
                setupOneMethod();
                setupMethodTwo();
            }

            private void setupMethodTwo() {
                MethodId methodId = new MethodId("module.dll","namespace","class","method2");
                methodRepository.addMethod("1",methodId );
            }
            
            private void setupUnitTests() {
                UnitTestMethodResult unitTestResult = new UnitTestMethodResult();
                unitTestResult.setNamespaceNameFromClassName("namespace").setClassName("class").setTestName("method1").setModuleFromCodeBase("a:\\module.dll");
                UnitTest test = new DefaultUnitTest("firsttest", unitTestResult);
            }
            

}
