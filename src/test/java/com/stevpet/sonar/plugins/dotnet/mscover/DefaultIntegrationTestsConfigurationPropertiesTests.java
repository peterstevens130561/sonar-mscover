package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.PropertyDefinition;

public class DefaultIntegrationTestsConfigurationPropertiesTests {

    private DefaultIntegrationTestsConfiguration integrationTestsConfiguration;
    
    @Before
    public void before() {
        integrationTestsConfiguration = new DefaultIntegrationTestsConfiguration();
        
    }
    
    @Test
    public void noDuplicates() {
        Map<String,PropertyDefinition> map = new HashMap<>();
        for(PropertyDefinition propertyDefinition:integrationTestsConfiguration.getProperties()) {
            String key = propertyDefinition.key();
            if(map.containsKey(key)) {
                Assert.fail("duplicate key " + key);
            }
            map.put(key, propertyDefinition);
        }
    }
}
