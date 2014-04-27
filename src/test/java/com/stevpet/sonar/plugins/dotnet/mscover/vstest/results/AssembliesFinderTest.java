package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.io.IOException;
import java.util.List;




import org.junit.Assert;
import org.junit.Test;
import org.sonar.test.TestUtils;


public class AssembliesFinderTest {

    @Test
    public void simpleFinder() throws IOException {
        AssembliesFinder finder = AssembliesFinder.create();
        finder.setPattern("**/bin/Debug/UnitTestProject");
        File root = TestUtils.getResource("Mileage");
        List<String> result=finder.findAssemblies(root);
        Assert.assertEquals(1, result.size());
    }
    
    @Test
    public void simpleFinderWild() throws IOException {
        AssembliesFinder finder = AssembliesFinder.create();
        finder.setPattern("**/bin/Debug/UnitTest*");
        File root = TestUtils.getResource("Mileage");
        List<String> result=finder.findAssemblies(root);
        Assert.assertEquals(1, result.size());
    }
    
    @Test
    public void simpleFinderWilder() throws IOException {
        AssembliesFinder finder = AssembliesFinder.create();
        finder.setPattern("**/bin/Debug/*Test*,**/bin/Debug/*Mileage*");
        File root = TestUtils.getResource("Mileage");
        List<String> result=finder.findAssemblies(root);
        Assert.assertEquals(3, result.size());
    }
    
    @Test
    public void simpleFinderExplicit_Nothin() throws IOException {
        AssembliesFinder finder = AssembliesFinder.create();
        finder.setPattern("unitTestProject");
        File root = TestUtils.getResource("Mileage");
        List<String> result=finder.findAssemblies(root);
        Assert.assertEquals(0, result.size());
    }
}
