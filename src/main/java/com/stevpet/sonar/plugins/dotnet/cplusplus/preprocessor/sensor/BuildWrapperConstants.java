package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import java.util.ArrayList;
import java.util.Collection;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyDefinition.Builder;

public class BuildWrapperConstants {
    public static final String BUILDWRAPPER_INSTALLDIR_KEY = "sonar.buildwrapper.buildwrapper.installdir";
    public static final String BUILDWRAPPER_MSBUILD_OPTIONS_KEY = "sonar.buildwrapper.msbuild.options";
    public static final String BUILDWRAPPER_OUTDIR_KEY = "sonar.buildwrapper.outputdir";
    public static final String BUILD_WRAPPER_CFAMILY_OUTPUT_KEY = "sonar.cfamily.build-wrapper-output";
    public static final String BUILDWRAPPER_ENABLED_KEY = "sonar.buildwrapper.enabled";
    
    public static Collection<PropertyDefinition> getProperties() {
        Collection<PropertyDefinition> properties = new ArrayList<>();
        properties.add(createProperty(BuildWrapperConstants.BUILDWRAPPER_ENABLED_KEY)
                .name("enabled")
                .description("set to true to enable build-wrapper")
                .type(PropertyType.BOOLEAN)
                .defaultValue("true")
                .build());
        properties.add(createProperty(BuildWrapperConstants.BUILDWRAPPER_INSTALLDIR_KEY)
                .name("build-wrapper install dir")
                .description("directory where build-wrapper is installed")
                .type(PropertyType.STRING)
                .build());
        properties.add(createProperty(BuildWrapperConstants.BUILDWRAPPER_OUTDIR_KEY)
                .name("output dir")
                .description("directory where build-wrapper results are stored, relative to solution")
                .type(PropertyType.STRING)
                .build());
        properties.add(createProperty(BuildWrapperConstants.BUILDWRAPPER_MSBUILD_OPTIONS_KEY)
                .name("msbuild options")
                .description("list of options to pass on to msbuild")
                .type(PropertyType.STRING)
                .build());
        return properties;

    }

    private static Builder createProperty(String key) {
        return PropertyDefinition.builder(key).subCategory("Build Wrapper");

    }
}
