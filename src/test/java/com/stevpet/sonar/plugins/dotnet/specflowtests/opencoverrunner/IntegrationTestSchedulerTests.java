package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;

public class IntegrationTestSchedulerTests {
    @Mock private IntegrationTestsConfiguration integrationTestsConfiguration ;
    private IntegrationTestsScheduler integrationTestScheduler ;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        integrationTestScheduler = new DefaultIntegrationTestsScheduler(integrationTestsConfiguration);
    }
    
    @Test
    public void notSpecifiedRunAlways() {
        
    }
}
