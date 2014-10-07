package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;

import org.sonar.api.config.Settings;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;

public class UnitTestRunnerTest {


    public void runTests_ShouldHaveFiles() {
        //Arrange
        Settings settings=UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();

        //Act
        VsTestRunner unitTestRunner = VsTestRunner.create();
        MsCoverProperties propertiesHelper = PropertiesHelper.create(settings);
        unitTestRunner.setPropertiesHelper(propertiesHelper);
        File solutionDirectory = TestUtils.getResource("Mileage");
        unitTestRunner.setSolutionDirectory(solutionDirectory);
        String sonarPath=TestUtils.getResource("Mileage/.sonar").getAbsolutePath();
        unitTestRunner.setSonarPath(sonarPath);
        unitTestRunner.runTests();    
        
    }
}
