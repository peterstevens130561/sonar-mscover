package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import java.util.ArrayList;
import java.util.Collection;

import org.sonar.api.BatchExtension;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

public class BuildWrapperConstants implements BatchExtension {
    
    public static final String INSTALLDIR_KEY = "sonar.buildwrapper.buildwrapper.installdir";
    public static final String MSBUILD_OPTIONS_KEY = "sonar.buildwrapper.msbuild.options";
    public static final String OUTDIR_KEY = "sonar.buildwrapper.outputdir";
    public static final String CFAMILY_OUTPUT_KEY = "sonar.cfamily.build-wrapper-output";
    public static final String ENABLED_KEY = "sonar.buildwrapper.enabled";
    public static final String PARALLEL_KEY = "sonar.buildwrapper.parallel";
    public static final String INSTALLPATH_KEY = "sonar.buildwrapper.buildwrapper.installpath";
    private Settings settings;

    public BuildWrapperConstants ( Settings settings) {
        this.settings = settings;
    }
    
    
    public static Collection<PropertyDefinition> getProperties() {
        Collection<PropertyDefinition> properties = new ArrayList<>();
        properties.add(createProperty(BuildWrapperConstants.ENABLED_KEY)
                .name("enabled")
                .index(0)
                .description("set to true to enable build-wrapper")
                .type(PropertyType.BOOLEAN)
                .defaultValue("true")
                .build());
        properties.add(createProperty(BuildWrapperConstants.INSTALLDIR_KEY)
                .name("build-wrapper install dir")
                .index(1)
                .description("directory where build-wrapper is installed")
                .type(PropertyType.STRING)
                .build());
        properties.add(createProperty(BuildWrapperConstants.INSTALLPATH_KEY)
                .name("build-wrapper install path")
                .index(2)
                .description("full path to exe (or bat)")
                .type(PropertyType.STRING)
                .build());
        properties.add(createProperty(BuildWrapperConstants.OUTDIR_KEY)
                .name("output dir")
                .index(3)
                .description("directory where build-wrapper results are stored, relative to solution")
                .type(PropertyType.STRING)
                .build());
        properties.add(createProperty(BuildWrapperConstants.MSBUILD_OPTIONS_KEY)
                .name("msbuild options")
                .index(4)
                .description("list of options to pass on to msbuild")
                .type(PropertyType.STRING)
                .build());
        properties.add(createProperty(BuildWrapperConstants.PARALLEL_KEY)
                .name("parallel")
                .index(5)
                .description("when set then buildwrapper can run in parallel")
                .type(PropertyType.BOOLEAN)
                .build());
        return properties;

    }

    private static Builder createProperty(String key) {
        return PropertyDefinition.builder(key).subCategory("Build Wrapper");

    }
    
    public boolean isParallelEnabled() {
        return settings.getBoolean(PARALLEL_KEY);
    }
}
