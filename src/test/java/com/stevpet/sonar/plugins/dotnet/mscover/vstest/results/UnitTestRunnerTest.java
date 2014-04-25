package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;

import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class UnitTestRunnerTest {

    @Test
    public void runTests_ShouldHaveFiles() {
        //Arrange
        Settings settings=UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();

        //Act
        UnitTestRunner unitTestRunner = UnitTestRunner.create();
        PropertiesHelper propertiesHelper = new PropertiesHelper(settings);
        unitTestRunner.setPropertiesHelper(propertiesHelper);
        File solutionDirectory = TestUtils.getResource("Mileage");
        unitTestRunner.setSolutionDirectory(solutionDirectory);
        unitTestRunner.runTests();    
        
    }
}
