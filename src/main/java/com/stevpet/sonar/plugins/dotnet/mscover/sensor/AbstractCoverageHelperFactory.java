package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public interface AbstractCoverageHelperFactory {
    CoverageHelper createIntegrationTestCoverageHelper(MsCoverProperties propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,MeasureSaver measureSaver);
    CoverageHelper createUnitTestCoverageHelper(MsCoverProperties propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,MeasureSaver measureSaver) ;  
    public  ShouldExecuteHelper createShouldExecuteHelper(
            MsCoverProperties propertiesHelper);
}
