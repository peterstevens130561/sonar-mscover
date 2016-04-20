package com.stevpet.sonar.plugins.dotnet.mscover;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;

public class CoverageReaderThreadsProperty extends ConfigurationPropertyBase<Integer> {

    public CoverageReaderThreadsProperty(Settings settings) {
        super(settings);

    }

    public CoverageReaderThreadsProperty() {
        super();
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(), PropertyType.INTEGER)
                .name("Number of threads for coverage reader")
                .description(
                        "Specified number of threads that the coverage reader uses when reading the coverage data for a project")
                .defaultValue("1");
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "coveragereader.threads";
    }

    @Override
    protected Integer onGetValue(Settings settings) {
        try {
            int threads = settings.getInt(getKey());
            if (threads <= 0 || threads >= 10) {
                throw new InvalidPropertyValueException(getKey(),settings.getString(getKey()),"must be integer between 1 and 10");
            }
            return threads;
        } catch (NumberFormatException e) {
            throw new InvalidPropertyValueException(getKey(),settings.getString(getKey()),"must be integer between 1 and 10");
        }
    }



}
