package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover.results;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.SensorContextMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;

public class OpenCoverCoverageResultsTest extends
        OpenCoverCoverageResultsBaseTest {
    
    private SensorContextMock sensorContextMock = new SensorContextMock();

    @Before() 
    public void before() {
        super.before();
    }
    
    @Test() 
    public void createWithEmptyMocks_ShouldCreate(){
        assertNotNull("sensor should be created with default mocks",sensor);
        
    }
    
    @Test
    public void shouldExecute_OpenCoverSet_True() {
        msCoverPropertiesMock.givenRunOpenCover(true);
        projectMock.givenIsRootProject(true);
        boolean result=sensor.shouldExecuteOnProject(projectMock.getMock());
        assertTrue("Configuration set to run opencover",result);
    }
    
    @Test
    public void shouldExecute_NotOpenCover_False() {
        msCoverPropertiesMock.givenRunOpenCover(false);
        projectMock.givenIsRootProject(true);
        boolean result=sensor.shouldExecuteOnProject(projectMock.getMock());
        assertFalse("Invoked on project,rather than solution",result);
    }
    @Test
    public void shouldExecute_NotRootProject_False() {
        msCoverPropertiesMock.givenRunOpenCover(true);
        projectMock.givenIsRootProject(false);
        boolean result=sensor.shouldExecuteOnProject(projectMock.getMock());
        assertFalse("Invoked on project,rather than solution",result);
    }
    
    @Test
    public void analyse() {
        vsTestEnvironmentMock.givenTestsHaveNotExecuted();
        sensor.analyse(projectMock.getMock(),sensorContextMock.getMock());
        
    }

}
