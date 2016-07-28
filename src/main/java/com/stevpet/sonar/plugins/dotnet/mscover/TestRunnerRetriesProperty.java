package com.stevpet.sonar.plugins.dotnet.mscover;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.property.ConfigurationPropertyBase;
import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;

public class TestRunnerRetriesProperty extends ConfigurationPropertyBase<Integer> {
    private static final String PRECONDITION = "must be int > -1";
    
    public TestRunnerRetriesProperty(Settings settings) {
        super(settings);
    }
    
    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(),PropertyType.INTEGER)
        .name("Number of retries for integrationtestrunner")
        .description(
                "Specified number of retries that the integrationtestrunner will perform on a project in case of a timeout")
        .defaultValue("3");
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "testrunner.retries";
    }

    @Override
    protected Integer onGetValue(Settings settings) {
        int retries=0;
        try {
            retries = settings.getInt(getKey());
        } catch (NumberFormatException e) {
            throw new InvalidPropertyValueException(getKey(), settings.getString(getKey()), PRECONDITION);
        }
        if (retries < 0) {
            throw new InvalidPropertyValueException(getKey(), settings.getString(getKey()), PRECONDITION);
        }
        return retries;
    }
}
