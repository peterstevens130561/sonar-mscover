package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIds;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTest;
import com.stevpet.sonar.plugins.dotnet.mscover.model.impl.DefaultMethodIds;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.MethodRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.SourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.UnitTestRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl.DefaultUnitTestResultService;

public class UnitTestResultsServiceSolitaryTests implements UnitTestResultsServiceTests {

    private DefaultUnitTestResultService service ;
    @Mock private MethodRepository methodRepository;
    @Mock private SourceFileRepository sourceFileRepository;
    @Mock private UnitTestRepository unitTestRepository;
    private String filePath;
    private MethodIds methods= new DefaultMethodIds();

    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        service = new DefaultUnitTestResultService(methodRepository, sourceFileRepository, unitTestRepository);
    }
    
    @Test
    @Override
    public void emptyRepositoriesExpectEmptyList() {
        filePath="booh";
        List<UnitTest> list = service.getUnitTestsFor(filePath);
        assertNotNull(list);
        assertEquals("expect empty list",0,list.size());
    }
    
    @Test
    @Override
    public void simpleOne() {
        setupOneSourceFile();
        setupOneMethod();
        List<UnitTest> unitTests = new ArrayList<>();
        when(unitTestRepository.getUnitTests(methods)).thenReturn(unitTests);
        
        List<UnitTest> result = service.getUnitTestsFor("booh");

        assertEquals("list should have same tests",result,unitTests);
        verify(methodRepository,times(1)).getMethods("1");
        verify(sourceFileRepository,times(1)).getId("booh");
    }

    private void setupOneMethod() {
        methods.add(new MethodId("m.dll","n","c","m"));
        when(methodRepository.getMethods("1")).thenReturn(methods);
    }
    
    @Test
    @Override
    public void unknownSourceFile() {
        when(sourceFileRepository.getId("booh")).thenReturn(null);
        MethodIds methods = new DefaultMethodIds();
        List<UnitTest> unitTests = new ArrayList<>();
        when(methodRepository.getMethods(null)).thenReturn(methods);
        when(unitTestRepository.getUnitTests(methods)).thenReturn(unitTests);
        
        List<UnitTest> result = service.getUnitTestsFor("booh");

        assertEquals("list should have same tests",result,unitTests);
        verify(methodRepository,times(1)).getMethods(null);
        verify(sourceFileRepository,times(1)).getId("booh");     
    }

    @Override
    public void sourceFileHasSomeTwoMethodsAndOneMethodHasTwoTestsShouldHaveThreeTests() {
        setupOneSourceFile();
        
    }

    private void setupOneSourceFile() {
        when(sourceFileRepository.getId("booh")).thenReturn("1");
    }
    
    
}
