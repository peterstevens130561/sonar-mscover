package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public interface AbstractCoverageHelperFactory {
    CoverageSaver createIntegrationTestCoverageHelper(MsCoverProperties propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,MeasureSaver measureSaver);
    CoverageSaver createUnitTestCoverageHelper(MsCoverProperties propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,MeasureSaver measureSaver) ;  
    
    ShouldExecuteHelper createShouldExecuteHelper(
            MsCoverProperties propertiesHelper);
}
