package com.stevpet.sonar.plugins.dotnet.mscover.property;

import java.io.File;

import org.assertj.core.util.Preconditions;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.config.PropertyDefinition.Builder;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class CoverageRootProperty implements ConfigurationProperty<File> {
    private Settings settings;
    public CoverageRootProperty(Settings settings) {
        this.settings = settings ;
    }
    
    public CoverageRootProperty() {
        this.settings = null;
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(), PropertyType.STRING)
                .name("root directory of integration test projects")
                .description("used in auto mode to determine whether to execute integration tests or save coverage data");
    }

    @Override
    public File getValue() {
        Preconditions.checkNotNull(settings);
        String key=getKey();
        String coveragePath = settings.getString(key);
        if(coveragePath == null) {
            throw new InvalidPropertyValueException(key, "property is required, and parent must exist");
        }
        File coverageDir = new File(coveragePath);
        File parentDir=coverageDir.getParentFile();
        if(parentDir==null) {
            throw new InvalidPropertyValueException(key, coveragePath,"must have parent");
        }
        if(!parentDir.exists()) {
            throw new InvalidPropertyValueException(key, coveragePath,"parent must exist");            
        }
        return coverageDir;
    }

    @Override
    public void validate() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER + "dir";
    }
    
    private static Builder createProperty(String key, PropertyType propertyType) {
        return PropertyDefinition.builder(key).type(propertyType).subCategory("Integration tests");
    }

}
