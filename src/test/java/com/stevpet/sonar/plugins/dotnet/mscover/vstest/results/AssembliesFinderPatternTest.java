package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.SonarException;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.anyString;

public class AssembliesFinderPatternTest {
    private Settings settings;
    private MsCoverProperties propertiesHelper ;
    private AssembliesFinder finder;
    
    @Before()
    public void before() {
        settings=mock(Settings.class);
        when(settings.getStringArrayBySeparator(anyString(), anyString())).thenCallRealMethod();
        propertiesHelper = PropertiesHelper.create(settings);
        finder = new AssembliesFinderFactory().create(propertiesHelper);
    }
    @Test
    public void simpleFinder() throws IOException {

        setPattern("**/bin/Debug/UnitTestProject");
        File root = TestUtils.getResource("Mileage");
        List<String> result=fromMSCoverProperty(root);
        Assert.assertEquals(1, result.size());
    }
    

    @Test
    public void simpleFinderWild() throws IOException {

        setPattern("**/bin/Debug/UnitTest*");
        File root = TestUtils.getResource("Mileage");
        List<String> result=fromMSCoverProperty(root);
        Assert.assertEquals(1, result.size());
    }
    
    @Test
    public void simpleFinderWilder() throws IOException {

        setPattern("**/bin/Debug/*Test*,**/bin/Debug/*Mileage*");
        File root = TestUtils.getResource("Mileage");
        List<String> result=fromMSCoverProperty(root);
        Assert.assertEquals(3, result.size());
    }
    
    @Test(expected=SonarException.class)
    public void simpleFinderExplicit_Nothin() throws IOException {

        setPattern("unitTestProject");
        File root = TestUtils.getResource("Mileage");
        List<String> result=fromMSCoverProperty(root);
    }
    
    private void setPattern(String pattern) {
        when(settings.getString(eq(PropertiesHelper.MSCOVER_UNITTEST_ASSEMBLIES))).thenReturn(pattern);   
    }
    
    private List<String> fromMSCoverProperty(File root) {
        return finder.findUnitTestAssembliesFromConfig(root);
    }
}
