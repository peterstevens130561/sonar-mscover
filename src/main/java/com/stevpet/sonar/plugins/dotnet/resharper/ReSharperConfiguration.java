/*
 * Sonar .NET Plugin :: ReSharper
 * Copyright (C) 2013 John M. Wright
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.stevpet.sonar.plugins.dotnet.resharper;

import java.util.ArrayList;
import java.util.Collection;

import org.sonar.api.BatchExtension;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Qualifiers;


/**
 * Constants & configuration of the ReSharper plugin. Class does no validation whatsooever of the properties
 */
public class ReSharperConfiguration implements BatchExtension {

    
    private Settings settings;

    public ReSharperConfiguration(Settings settings) {
        this.settings = settings;
    }
    public static final String DEFAULT_CACHEDIR = "inspectcode_cache";
    public static final String REPOSITORY_KEY = "resharper";
    public static final String REPOSITORY_NAME = "ReSharper";
    public static final String[] SUPPORTED_LANGUAGES = new String[] { "cs", "vbnet" };

    // ----------- Plugin Configuration Properties ----------- //
    public static final String MODE = "sonar.resharper.mode";
    public static final String ENABLED_KEY = "sonar.resharper.enabled";
    public static final String REPORTS_PATH_KEY = "sonar.resharper.reports.path";
    public static final String REPORT_FILENAME = "resharper-report.xml";

    static final String INSTALL_DIR_KEY = "sonar.resharper.installDirectory";
    public static final String INSTALL_DIR_DEFVALUE = "C:/jetbrains-commandline-tools";

    private static final String TIMEOUT_MINUTES_KEY = "sonar.resharper.timeoutMinutes";
    private static final int TIMEOUT_MINUTES_DEFVALUE = 20;

    public static final String CUSTOM_RULES_PROP_KEY = "sonar.resharper.customRules.definition";

    public static final String CUSTOM_SEVERITIES_PROP_KEY = "sonar.resharper.customSeverities.definition";
    public static final String PROFILE_NAME = "sonar.resharper.profile";
    public static final String PROFILE_DEFVALUE = "Sonar Way";

    public static final String INCLUDE_ALL_FILES = "sonar.resharper.includeAllFiles";
    public static final String FAIL_ON_ISSUES_KEY = "sonar.resharper.failOnIssues";
    public static final String CUSTOM_SEVERITIES_DEFINITON = "sonar.resharper.customSeverities.definition";
    public static final String CUSTOM_SEVERITIES_PATH = "sonar.resharper.customSeverities.path";
    private static final String INSPECTCODE_PROPERTIES_KEY = "sonar.resharper.inspectcode.properties";
    public static final String PROFILE_DEFAULT = "Sonar way";
    public static final String CACHES_HOME = "sonar.resharper.inspectcode.cacheshome";
    public static final String DOTSETTINGS_KEY = "sonar.resharper.inspectcode.profile";
    public static final String MODE_REUSE_REPORT = "resuseReport";
    public static final String DEFAULT_RULES = "/ReSharper/DefaultRules.ReSharper";
    public static final String MODE_SKIP = "skip";
    public static final String BUILD_CONFIGURATION_KEY = "sonar.dotnet.buildConfiguration";
    public static final String BUILD_CONFIGURATIONS_DEFVALUE = "Debug";

    public static final String BUILD_PLATFORM_KEY = "sonar.dotnet.buildPlatform";
    public static final String BUILD_PLATFORM_DEFVALUE = "x64";
    public static final String FAIL_ON_EXCEPTION_KEY = "sonar.resharper.failonexception";
    public static final String USE_CACHE_KEY="sonar.resharper.usecache";

    /**
     * global setting indicates that the analysis should fail when an exception is thrown in the sensor
     * @return 
     */
    public boolean failOnException() {
        return settings.getBoolean(FAIL_ON_EXCEPTION_KEY);
    }
    
    /**
     * 
     * @return absolute path to installation directory of inspectcode 
     */
    public String getInspectCodeInstallDir() {
        return settings.getString(ReSharperConfiguration.INSTALL_DIR_KEY);
    }
    
    public static Collection<PropertyDefinition> getProperties() {
        Collection<PropertyDefinition> properties = new ArrayList<>();
        properties.add(createProperty(ENABLED_KEY, PropertyType.BOOLEAN)
                .name("enabled")
                .index(0)
                .onQualifiers(Qualifiers.PROJECT)
                .description("set to true to enable resharper plugin")
                .defaultValue("true")
                .build());
        properties.add(createProperty(FAIL_ON_EXCEPTION_KEY, PropertyType.BOOLEAN)
                .name("fail analysis on thrown exception")
                .index(1)
                .description("")
                .onQualifiers(Qualifiers.PROJECT)
                .defaultValue("true")
                .build());
        properties.add(createProperty(REPORTS_PATH_KEY, PropertyType.STRING)
                .name("report file(s)")
                .index(2)
                .description("local path of the ReSharper report file(s) used when reuse report mode is activated. "
                        + "This can be an absolute path, or a path relative to each project base directory.")
                .build());
        properties.add(createProperty(INSTALL_DIR_KEY, PropertyType.STRING)
                .defaultValue(ReSharperConfiguration.INSTALL_DIR_DEFVALUE)
                .index(3)
                .name("local inspectcode install directory").description("Absolute path of the ReSharper Command Line Tools installation folder.")
                .build());
        properties.add(createProperty(TIMEOUT_MINUTES_KEY, PropertyType.INTEGER)
                .defaultValue(TIMEOUT_MINUTES_DEFVALUE + "")
                .index(4)
                .onQualifiers(Qualifiers.PROJECT)
                .name("execution timeout")
                .description("Maximum number of minutes before the ReSharper program will be stopped.").build());
        properties.add(createProperty(DOTSETTINGS_KEY,PropertyType.STRING)
                .name("profile")
                .index(5)
                .description("local path to .DotSettings file")
                .onQualifiers(Qualifiers.PROJECT)
                .build());
        properties.add(createProperty(USE_CACHE_KEY,PropertyType.BOOLEAN)
                .name("use cache")
                .index(6)
                .onQualifiers(Qualifiers.PROJECT)
                .defaultValue("false")
                .description("when set a cache is used, which speeds up analysis, but may give funny results. Recommeded to leave false").build());
        properties.add(createProperty(CACHES_HOME,PropertyType.STRING)
                .name("caches home")
                .index(7)
                .onQualifiers(Qualifiers.PROJECT)
                .description("local absolute path to inspectcode cache, change when .DotSettings file has changed").build());
        properties.add(createProperty(INSPECTCODE_PROPERTIES_KEY,PropertyType.STRING)
                .name("msbuild properties")
                .index(8)
                .description("additional properties to pass on to msbuild").build());
     
        return properties;

    }


    private static Builder createProperty(String key, PropertyType propertyType) {
        return PropertyDefinition.builder(key).type(propertyType).subCategory("Re#");

    }

    public String getMSBuildProperties() {
        return settings.getString(INSPECTCODE_PROPERTIES_KEY);
    }

    /**
     * @return path to the dotsettings file
     */
    public String getProfile() {
        return settings
                .getString(DOTSETTINGS_KEY);
    }
    
    public int getTimeOutMinutes() {
        return settings.getInt(TIMEOUT_MINUTES_KEY);
    }

    public String getCachesHome() {
        return settings
        .getString(ReSharperConfiguration.CACHES_HOME);
    }

    public boolean useCache() {
        return settings.getBoolean(USE_CACHE_KEY);
    }
}