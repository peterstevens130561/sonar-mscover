package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.test.TestUtils;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;


import static org.mockito.Mockito.mock;
public class AssembliesFinderTest {
    private Settings settings;
    private PropertiesHelper propertiesHelper ;
    
    @Before()
    public void before() {
        settings=mock(Settings.class);
        propertiesHelper = PropertiesHelper.create(settings);
    }
    @Test
    public void simpleFinder() throws IOException {
        AssembliesFinder finder = AssembliesFinder.create(propertiesHelper);
        finder.setPattern("**/bin/Debug/UnitTestProject");
        File root = TestUtils.getResource("Mileage");
        List<String> result=finder.findAssemblies(root);
        Assert.assertEquals(1, result.size());
    }
    
    @Test
    public void simpleFinderWild() throws IOException {
        AssembliesFinder finder = AssembliesFinder.create(propertiesHelper);
        finder.setPattern("**/bin/Debug/UnitTest*");
        File root = TestUtils.getResource("Mileage");
        List<String> result=finder.findAssemblies(root);
        Assert.assertEquals(1, result.size());
    }
    
    @Test
    public void simpleFinderWilder() throws IOException {
        AssembliesFinder finder = AssembliesFinder.create(propertiesHelper);
        finder.setPattern("**/bin/Debug/*Test*,**/bin/Debug/*Mileage*");
        File root = TestUtils.getResource("Mileage");
        List<String> result=finder.findAssemblies(root);
        Assert.assertEquals(3, result.size());
    }
    
    @Test
    public void simpleFinderExplicit_Nothin() throws IOException {
        AssembliesFinder finder = AssembliesFinder.create(propertiesHelper);
        finder.setPattern("unitTestProject");
        File root = TestUtils.getResource("Mileage");
        List<String> result=finder.findAssemblies(root);
        Assert.assertEquals(0, result.size());
    }
}
