package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.MeasureSaverMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class SonarCoverageHelperFactoryTest {

    private MeasureSaverMock measureSaverMock = new MeasureSaverMock();
    private MeasureSaver measureSaver;
    private FileSystemMock fileSystemMock = new FileSystemMock();
    private AbstractCoverageHelperFactory factory ;
    
    @Before
    public void before() {
        measureSaver = measureSaverMock.getMock();
        factory = new SonarCoverageHelperFactory() ;
    }

    @Test

    public void createIntegrationTestCoverageHelper_DoesNotActOnDependencies() {
        CoverageSaver coverageHelper = factory.createVsTestIntegrationTestCoverageHelper(fileSystemMock.getMock(), measureSaver);
        assertNotNull(coverageHelper);

    }
    
    @Test
    public void createUnitTestCoverageHelper_DoesNotActOnDependencies() {
        CoverageSaver coverageHelper = factory.createUnitTestCoverageHelper(fileSystemMock.getMock(), measureSaver);
        assertNotNull(coverageHelper);
    }
}
