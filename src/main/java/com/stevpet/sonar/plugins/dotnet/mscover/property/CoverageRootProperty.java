package com.stevpet.sonar.plugins.dotnet.mscover.property;

import java.io.File;
import org.sonar.api.PropertyType;
import org.sonar.api.config.Settings;
import org.sonar.api.config.PropertyDefinition.Builder;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class CoverageRootProperty extends ConfigurationPropertyBase<File> {
    
    public CoverageRootProperty(Settings settings) {
        super(settings);
    }
    
    public CoverageRootProperty() {
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
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER + "dir";
    }
    

}
