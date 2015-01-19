package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.MeasureSaverMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class SonarCoverageHelperFactoryTest {

    private MeasureSaverMock measureSaverMock = new MeasureSaverMock();
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private MeasureSaver measureSaver;
    private MsCoverProperties propertiesHelper;
    private FileSystemMock fileSystemMock = new FileSystemMock();
    private AbstractCoverageHelperFactory factory ;
    
    @Before
    public void before() {
        measureSaver = measureSaverMock.getMock();
        propertiesHelper = msCoverPropertiesMock.getMock();
        factory = new SonarCoverageHelperFactory() ;
    }

    @Test

    public void createIntegrationTestCoverageHelper_DoesNotActOnDependencies() {
        CoverageSaver coverageHelper = factory.createIntegrationTestCoverageHelper(propertiesHelper, fileSystemMock.getMock(), measureSaver);
        assertNotNull(coverageHelper);

    }
    
    @Test
    public void createUnitTestCoverageHelper_DoesNotActOnDependencies() {
        CoverageSaver coverageHelper = factory.createUnitTestCoverageHelper(propertiesHelper, fileSystemMock.getMock(), measureSaver);
        assertNotNull(coverageHelper);
    }
}
