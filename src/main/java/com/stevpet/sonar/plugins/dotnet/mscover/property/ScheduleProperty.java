package com.stevpet.sonar.plugins.dotnet.mscover.property;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;


public class ScheduleProperty extends ConfigurationPropertyBase<Pattern> {

    public ScheduleProperty(Settings settings) {
        super(settings);
    }
    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(), PropertyType.REGULAR_EXPRESSION)
                .name("Pattern to specify schedule")
                .description("Regular expression to specify the day on which integration tests are run\n" +
                        "1=MONDAY, 2=Tuesday i.e. [6-7] = SATURDAY and SUNDAY")
                .defaultValue(".*");
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER + "schedule";
    }

    @Override
    protected Pattern onGetValue(Settings settings) {
        String schedule = settings.getString(getKey());
        if ("*".equals(schedule)) {
            schedule = ".*";
        }
        try {
            Pattern p = Pattern.compile(schedule);
            return p;
        } catch (PatternSyntaxException e) {
            throw new InvalidPropertyValueException(getKey(), schedule, "valid regular expression", e);
        }
    }

}
