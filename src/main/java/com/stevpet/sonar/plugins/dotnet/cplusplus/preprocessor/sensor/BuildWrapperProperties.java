package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;

public class BuildWrapperProperties {

    public void something(){
        PropertyDefinition
        .builder(BuildWrapperConstants.BUILDWRAPPER_INSTALLDIR_KEY)
        .defaultValue("buildwrapper-output")
        .description("directory relative to .sonar dir where buildwrapper stores its output")
        .type(PropertyType.STRING).build();
    }
}
