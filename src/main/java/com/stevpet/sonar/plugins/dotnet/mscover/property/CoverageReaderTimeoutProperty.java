package com.stevpet.sonar.plugins.dotnet.mscover.property;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class CoverageReaderTimeoutProperty extends ConfigurationPropertyBase<Integer> {

    private Settings settings;

    public CoverageReaderTimeoutProperty(Settings settings) {
        super(settings);
        this.settings = settings;
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(), PropertyType.INTEGER)
        .name("Timeout for coverage reader")
        .description("Specifies max time that the coveragereader may take to parse all coverage data of a project")
        .defaultValue("4");
    }

    @Override
    public Integer onGetValue(Settings settings) {
        int timeout = settings.getInt(getKey());
        if(timeout <0) {
            throw new InvalidPropertyValueException(getKey(),timeout,"must be positive integer");
        }
        if (timeout == 0) {
            timeout = 5;
        }
        return timeout;
    }

    @Override
    public void validate() {
        getValue();
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "coveragereader.timeout";
    }



}
