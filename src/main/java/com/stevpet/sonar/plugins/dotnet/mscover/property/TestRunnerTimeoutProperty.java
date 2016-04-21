package com.stevpet.sonar.plugins.dotnet.mscover.property;

import javax.annotation.Nonnull;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class TestRunnerTimeoutProperty extends ConfigurationPropertyBase<Integer> {
    private static final String PROPERTY_KEY = DefaultIntegrationTestsConfiguration.MSCOVER
            + "testrunner.timeout";
    
    /**
     * regular use
     * @param settings - valid settings
     */
    public TestRunnerTimeoutProperty(@Nonnull Settings settings) {
        super(settings);
    }
    
    /**
     * use only to get the PropertyBuilder
     */
    public TestRunnerTimeoutProperty() {
        super();
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
    public Integer onGetValue(Settings settings) {
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


    @Override
    public String getKey() {
        return PROPERTY_KEY;
    }

}
