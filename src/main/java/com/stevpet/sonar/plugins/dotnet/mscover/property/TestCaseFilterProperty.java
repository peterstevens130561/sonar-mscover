package com.stevpet.sonar.plugins.dotnet.mscover.property;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class TestCaseFilterProperty extends ConfigurationPropertyBase<String> {


    public TestCaseFilterProperty(Settings settings) {
        super(settings);
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(),PropertyType.STRING).name("Testcasefilter").description("filter to apply to VSTEST for testcases");
    }


    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "testcasefilter";
    }

    @Override
    protected String onGetValue(Settings settings) {
        return settings.getString(getKey());
    }

}
