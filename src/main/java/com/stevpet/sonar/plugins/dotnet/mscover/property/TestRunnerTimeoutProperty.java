package com.stevpet.sonar.plugins.dotnet.mscover.property;

import javax.annotation.Nonnull;

import org.assertj.core.util.Preconditions;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class TestRunnerTimeoutProperty implements ConfigurationProperty<Integer> {
    private static final String PROPERTY_KEY = DefaultIntegrationTestsConfiguration.MSCOVER
            + "testrunner.timeout";
    private Settings settings;
    
    /**
     * regular use
     * @param settings - valid settings
     */
    public TestRunnerTimeoutProperty(@Nonnull Settings settings) {
        this.settings = settings;
    }
    
    /**
     * use only to get the PropertyBuilder
     */
    public TestRunnerTimeoutProperty() {
        this.settings = null;
    }
    
    @Override
    public  Builder getPropertyBuilder() {
        return createProperty(PROPERTY_KEY, PropertyType.INTEGER)
                .name("Timeout for coverage reader")
                .description(
                        "Specifies max time in minutes that the testrunner may take to run all integrationtests in a solution")
                .defaultValue("120");
    }

    @Override
    public Integer getValue() {
        Preconditions.checkNotNull(settings);
        int timeout=0;
        try {
        timeout = settings.getInt(PROPERTY_KEY);
        } catch ( NumberFormatException e) {
            throw new InvalidPropertyValueException(PROPERTY_KEY,settings.getString(PROPERTY_KEY),"number >0 and <=120 minutes");
        }
        if(timeout < 0 || timeout > 120) {
            throw new InvalidPropertyValueException(PROPERTY_KEY, timeout, "number >0 and <=120 minutes");           
        }
        return timeout;
    }

    private static Builder createProperty(String key, PropertyType propertyType) {
        return PropertyDefinition.builder(key).type(propertyType).subCategory("Integration tests");
    }

    @Override
    public String getKey() {
        return PROPERTY_KEY;
    }

    @Override
    public void validate() {
        getValue();
    }
}
