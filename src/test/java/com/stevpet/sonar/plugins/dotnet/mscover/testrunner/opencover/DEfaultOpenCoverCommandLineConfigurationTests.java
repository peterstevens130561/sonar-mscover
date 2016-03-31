package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.config.Settings;
import static org.mockito.Mockito.when;
public class DEfaultOpenCoverCommandLineConfigurationTests {

    private OpenCoverCommandLineConfiguration configuration ;
    @Mock private Settings settings ;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        configuration = new DefaultOpenCoverCommandLineConfiguration(settings);
    }
    
    @Test
    public void noInstallDirSpecified() {
        try {
            configuration.getInstallDir();
        } catch (IllegalArgumentException e) {
            assertEquals("expected message","property sonar.mscover.opencover.installDirectory not set",e.getMessage());
            return;
        }
        fail("expected message");
    }
    
    @Test
    public void emptyInstallDirSpecified() {
        when(settings.getString("sonar.mscover.opencover.installDirectory")).thenReturn("");
        try {
            configuration.getInstallDir();
        } catch (IllegalArgumentException e) {
            assertEquals("expected message","property sonar.mscover.opencover.installDirectory not set",e.getMessage());
            return;
        }
        fail("expected message");
    }
    @Test
    public void installDirSpecified() {
        String value = "C:/bogus" ;
        when(settings.getString("sonar.mscover.opencover.installDirectory")).thenReturn(value);
        String installDir=configuration.getInstallDir();
        assertEquals(value,installDir);

    }
}
