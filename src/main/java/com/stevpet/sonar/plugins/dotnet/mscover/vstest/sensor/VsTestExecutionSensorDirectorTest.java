package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;


import com.stevpet.sonar.plugins.dotnet.mscover.utils.SensorTest;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;

public class VsTestExecutionSensorDirectorTest extends SensorTest {

    private VsTestExecutionSensorDirector director = new VsTestExecutionSensorDirector();
    private DefaultPicoContainer container;
    @Before
    public void before() {
        container = super.getContainerWithSensorMocks();
        director.wire(container);

    }
    
    
    @Test
    public void createRunner() {
        //given the container is initialized
        //when I create the unit test runner
        VsTestRunner unitTestRunner = container.getComponent(VsTestRunner.class);
        //then 
        assertNotNull("create WindowsVsTestRunner",unitTestRunner);
    }
}
