package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

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
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl.UnitTestRepository;

public class UnitTestResultsServiceTests {

    private DefaultUnitTestResultService service ;
    @Mock private MethodRepository methodRepository;
    @Mock private SourceFileRepository sourceFileRepository;
    @Mock private UnitTestRepository unitTestRepository;
    private String filePath;

    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        service = new DefaultUnitTestResultService(methodRepository, sourceFileRepository, unitTestRepository);
    }
    
    @Test
    public void emptyRepositoriesExpectEmptyList() {
        filePath="booh";
        List<UnitTest> list = service.getUnitTestsFor(filePath);
        assertNotNull(list);
        assertEquals("expect empty list",0,list.size());
    }
    
    @Test
    public void simpleOne() {
        when(sourceFileRepository.getId("booh")).thenReturn("1");
        List<MethodId> methods = new ArrayList<>();
        List<UnitTest> unitTests = new ArrayList<>();
        when(methodRepository.getMethods("1")).thenReturn(methods);
        when(unitTestRepository.getUnitTests(methods)).thenReturn(unitTests);
        
        List<UnitTest> result = service.getUnitTestsFor("booh");

        assertEquals("list should have same tests",result,unitTests);
        verify(methodRepository,times(1)).getMethods("1");
        verify(sourceFileRepository,times(1)).getId("booh");
    }
    
    @Test
    public void unknownSourceFile() {
        when(sourceFileRepository.getId("booh")).thenReturn(null);
        List<MethodId> methods = new ArrayList<>();
        List<UnitTest> unitTests = new ArrayList<>();
        when(methodRepository.getMethods(null)).thenReturn(methods);
        when(unitTestRepository.getUnitTests(methods)).thenReturn(unitTests);
        
        List<UnitTest> result = service.getUnitTestsFor("booh");

        assertEquals("list should have same tests",result,unitTests);
        verify(methodRepository,times(1)).getMethods("1");
        verify(sourceFileRepository,times(1)).getId("booh");     
    }
    
    
}
