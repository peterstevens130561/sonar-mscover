package com.stevpet.sonar.plugins.dotnet.mscover.property;

import java.util.regex.Pattern;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class ProjectPatternProperty extends ConfigurationPropertyBase<Pattern> {

    public ProjectPatternProperty(Settings settings) {
        super(settings);
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(), PropertyType.REGULAR_EXPRESSION)
                .name("Pattern for integration test projects")
                .description(
                        "Regular expression to determine which projects are integration test projects, should also be included in overall test project pattern");
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "projectpattern";
    }

    @Override
    protected Pattern onGetValue(Settings settings) {
        Pattern pattern = getPattern(getKey());
        return pattern;
    }
    
    

}
