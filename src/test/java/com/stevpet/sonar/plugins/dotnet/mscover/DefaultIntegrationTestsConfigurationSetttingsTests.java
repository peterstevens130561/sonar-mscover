package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.PropertyDefinitions;
import org.sonar.api.config.Settings;

public class DefaultIntegrationTestsConfigurationSetttingsTests  {

    private static final String SCHEDULE_PROPERTY_KEY = "sonar.mscover.integrationtests.schedule";
    private Settings settings ;
    private IntegrationTestsConfiguration integrationTestsConfiguration;
    @Mock private FileSystem fileSystem;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        PropertyDefinitions definitions = new PropertyDefinitions(DefaultIntegrationTestsConfiguration.getProperties());
        settings = new Settings(definitions);
        integrationTestsConfiguration = new DefaultIntegrationTestsConfiguration(settings, fileSystem);
        
    }
    
    @Test
    public void scheduleNotSetExpectDefault() {
        Pattern defaultPattern = integrationTestsConfiguration.getSchedule();
        assertEquals("property not set",".*",defaultPattern.pattern());
    }
    
    @Test
    public void simplePattern() {
        settings.appendProperty(SCHEDULE_PROPERTY_KEY, "[6-7]");
        assertEquals("[6-7]",integrationTestsConfiguration.getSchedule().pattern());
    }
    
    @Test
    public void invalidPattern() {
        settings.appendProperty(SCHEDULE_PROPERTY_KEY, "[6-7");
        try {
            integrationTestsConfiguration.getSchedule().pattern();
        } catch (InvalidPropertyValueException e ) {
            assertEquals(SCHEDULE_PROPERTY_KEY,e.getPropertyKey());
            assertEquals("[6-7",e.getPropertyValue());
            return;
        }
        fail("expected InvalidPropertyException");
    }
}
