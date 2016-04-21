package com.stevpet.sonar.plugins.dotnet.mscover.property;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class SpecflowTestsRootProperty extends ConfigurationPropertyBase<File> {

    private static final String PRECONDITION_TEXT = "must be directory and must exist";

    public SpecflowTestsRootProperty(Settings settings) {
        super(settings);
    }
    public SpecflowTestsRootProperty() {
        super();
    }
    
    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(), PropertyType.STRING)
                .name("root directory of integration test projects")
                .description("used in auto mode to determine whether to execute integration tests or save coverage data");
    }

    @Override
    public File onGetValue(Settings settings) {
        String path=settings.getString(getKey());
        if(StringUtils.isEmpty(path)) {
            return null;
        }
        File root = new File(path);
        if(!root.exists() || !root.isDirectory()) {
            throw new InvalidPropertyValueException(getKey(), path,PRECONDITION_TEXT);
        }
        return root;
    }

    @Override
    public void validate() {
        getValue();    
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER + "root";
    }

}
