package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.test.TestUtils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class SpecFlowScenarioMethodResolverTest {

    private SpecFlowScenarioMethodResolver resolver ;
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        resolver = new SpecFlowScenarioMethodResolver(microsoftWindowsEnvironment);
    }
    
    @Test
    public void NoUnitTestSourceFiles() {
        when(microsoftWindowsEnvironment.getUnitTestSourceFiles()).thenReturn(new ArrayList<File>());
        File file=resolver.getFile("bogusmethod");
        assertNull("no sourcefiles, so should not find it",file);  
    }
    
    @Test
    public void IllegalFeatureFile() {
        givenIllegalFeatureFile();
        try {
            File file=resolver.getFile("bogusmethod"); 
            } catch(MsCoverException e) {
                return;
            }
        fail("expect MSCoverException on invalid file");  
    }
    @Test
    public void OneUnitTestSourceFiles() {
        givenFeatureFile();
        File file=resolver.getFile("bogusmethod");
        assertNull("no sourcefiles, so should not find it",file);  
    }

    @Test
    public void Find_ConvertFromDegreesToRadians_180() {
        givenFeatureFile();
        String name="ConvertFromDegreesToRadians_180";
        File file=resolver.getFile(name);
        assertNotNull("should find method "+ name,file);  
    }
    
    @Test
    public void Find_ConvertFromDegreesToRadians_270() {
        givenFeatureFile();
        String name="ConvertFromDegreesToRadians_270";
        File file=resolver.getFile(name);
        assertNotNull("should find method "+ name,file);  
    }
    @Test
    public void IncompleteBegin_ShouldNotFind() {
        givenFeatureFile();
        String name="onvertFromDegreesToRadians_180";
        File file=resolver.getFile(name);
        assertNull("should notfind method, as it is incomplete "+ name,file);  
    }
    
    @Test
    public void IncompleteEnd_ShouldNotFind() {
        givenFeatureFile();
        String name="ConvertFromDegreesToRadians_18";
        File file=resolver.getFile(name);
        assertNull("should notfind method, as it is incomplete "+ name,file);  
    }
    
    public void givenIllegalFeatureFile() {
        List<File> files = new ArrayList<>();
        File featureFile = new File("!@#$#DE#");
        assertNotNull("should have file",featureFile);
        files.add(featureFile);     
        when(microsoftWindowsEnvironment.getUnitTestSourceFiles()).thenReturn(files);
    }
    public void givenFeatureFile() {
        List<File> files = new ArrayList<>();
        File featureFile = TestUtils.getResource("SpecFlowScenarioMethodResolver/Conversion.feature.cs");
        assertNotNull("should have file",featureFile);
        files.add(featureFile);     
        when(microsoftWindowsEnvironment.getUnitTestSourceFiles()).thenReturn(files);
    }
}
