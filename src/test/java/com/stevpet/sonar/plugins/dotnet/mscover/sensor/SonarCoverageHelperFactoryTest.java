package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.MeasureSaverMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class SonarCoverageHelperFactoryTest {

    private MeasureSaverMock measureSaverMock = new MeasureSaverMock();
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private MeasureSaver measureSaver;
    private MsCoverProperties propertiesHelper;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private AbstractCoverageHelperFactory factory ;
    
    @Before
    public void before() {
        measureSaver = measureSaverMock.getMock();
        propertiesHelper = msCoverPropertiesMock.getMock();
        microsoftWindowsEnvironment = microsoftWindowsEnvironmentMock.getMock();
        factory = new SonarCoverageHelperFactory() ;
    }

    @Test

    public void createIntegrationTestCoverageHelper_DoesNotActOnDependencies() {
        CoverageSaver coverageHelper = factory.createIntegrationTestCoverageHelper(propertiesHelper, microsoftWindowsEnvironment, measureSaver);
        assertNotNull(coverageHelper);

    }
    
    @Test
    public void createUnitTestCoverageHelper_DoesNotActOnDependencies() {
        CoverageSaver coverageHelper = factory.createUnitTestCoverageHelper(propertiesHelper, microsoftWindowsEnvironment, measureSaver);
        assertNotNull(coverageHelper);
    }
}
