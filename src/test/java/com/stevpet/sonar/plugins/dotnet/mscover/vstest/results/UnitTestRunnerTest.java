package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;

import org.sonar.api.config.Settings;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;

public class UnitTestRunnerTest {


    public void runTests_ShouldHaveFiles() {
        //Arrange
        MsCoverProperties properties=UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();

        //Act
        VsTestRunner unitTestRunner = WindowsVsTestRunner.create();
        unitTestRunner.setPropertiesHelper(properties);
        File solutionDirectory = TestUtils.getResource("Mileage");
        unitTestRunner.setSolutionDirectory(solutionDirectory);
        String sonarPath=TestUtils.getResource("Mileage/.sonar").getAbsolutePath();
        unitTestRunner.setSonarPath(sonarPath);
        unitTestRunner.runTests();    
        
    }
}
