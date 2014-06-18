package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public interface AbstractCoverageHelperFactory {
    CoverageHelper createIntegrationTestCoverageHelper(PropertiesHelper propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,MeasureSaver measureSaver);
    CoverageHelper createUnitTestCoverageHelper(PropertiesHelper propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,MeasureSaver measureSaver) ;  
    public  ShouldExecuteHelper createShouldExecuteHelper(
            PropertiesHelper propertiesHelper);
}
