package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.config.Settings;


public class SettingsHelperTest {

    @Mock private Settings settings;
    private static String SONAR_MSCOVER_INTEGRATIONTESTS_PATTERN = "some";
    
    private SettingsHelper configuration ;
    @Before()
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        configuration = new SettingsHelper(settings);
    }
    
    @Test
    public void illegalPattern() {
        when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_PATTERN)).thenReturn("[illegal");
        try {
            configuration.getPattern(SONAR_MSCOVER_INTEGRATIONTESTS_PATTERN);
        } catch(IllegalStateException e) {

            return;
        }
        fail("expected IllegalStateException");
    }
    
       @Test
        public void noPattern() {
            when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_PATTERN)).thenReturn(null);
            Pattern p=    configuration.getPattern(SONAR_MSCOVER_INTEGRATIONTESTS_PATTERN);
            assertNull("expect null as no pattern is specified",p);
        }
       
       @Test
       public void validPattern() {
           when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_PATTERN)).thenReturn(".*SpecFlowTests.*");
           Pattern p=    configuration.getPattern(SONAR_MSCOVER_INTEGRATIONTESTS_PATTERN);
           assertEquals("expect valid pattern is specified",".*SpecFlowTests.*",p.toString());
       }
}
