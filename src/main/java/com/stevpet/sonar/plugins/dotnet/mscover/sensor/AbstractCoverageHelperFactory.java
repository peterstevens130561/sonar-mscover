package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public interface AbstractCoverageHelperFactory {
    CoverageSaver createIntegrationTestCoverageHelper(
            FileSystem fileSystem,MeasureSaver measureSaver);
    CoverageSaver createUnitTestCoverageHelper(
            FileSystem fileSystem,MeasureSaver measureSaver) ;  
    
    ShouldExecuteHelper createShouldExecuteHelper(
            MsCoverProperties propertiesHelper);
}
