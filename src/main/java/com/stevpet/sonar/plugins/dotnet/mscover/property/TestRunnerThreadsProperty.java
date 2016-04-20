package com.stevpet.sonar.plugins.dotnet.mscover.property;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class TestRunnerThreadsProperty extends ConfigurationPropertyBase<Integer> {

    private static final String PRECONDITION = "integer >0 and <=10";

    public TestRunnerThreadsProperty(Settings settings) {
        super(settings);
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(),PropertyType.INTEGER)
        .name("Number of threads for integrationtestrunner")
        .description(
                "Specified number of threads that the integrationtestrunner uses when reading the coverage data for a project")
        .defaultValue("1");
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "testrunner.threads";
    }

    @Override
    protected Integer onGetValue(Settings settings) {
        int threads=0;
        try {
            threads = settings.getInt(getKey());
        } catch (NumberFormatException e) {
            throw new InvalidPropertyValueException(getKey(), settings.getString(getKey()), PRECONDITION);
        }
        if (threads < 0 || threads > 10) {
            throw new InvalidPropertyValueException(getKey(), settings.getString(getKey()), PRECONDITION);
        }
        return threads;
    }

}
