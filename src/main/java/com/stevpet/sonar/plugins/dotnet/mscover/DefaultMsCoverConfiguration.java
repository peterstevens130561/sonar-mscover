/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.PropertyType;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.resources.Qualifiers;
@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class DefaultMsCoverConfiguration implements BatchExtension, MsCoverConfiguration  {

    private Logger LOG = LoggerFactory.getLogger(DefaultMsCoverConfiguration.class);
    
    public enum RunMode {
        SKIP,
        REUSE, // NO_UCD (test only)
        RUNVSTEST,
        NULL // NO_UCD (unused code)
    }
    private final Settings settings;
    private SettingsHelper settingsHelper;
    private static final String MSCOVER = "sonar.mscover.";

    public static final String MSCOVER_UNIT_COVERAGEXML_PATH=MSCOVER + "unittests.coveragexml";
    public static final String MSCOVER_EXECUTEROOT = MSCOVER + "rootproject";
    public static final String MSCOVER_EXCLUSIONS=MSCOVER + "exclusions";
    public static final String MSCOVER_INCLUSIONS=MSCOVER + "inclusions";
    public static final String MSCOVER_CUTOFFDATE=MSCOVER + "cutoffdate" ;
    public static final String MSCOVER_INTEGRATION_COVERAGEXML_PATH = MSCOVER + "integrationtests.coveragexml";
    static final String MSCOVER_INTEGRATION_VSTESTDIR= MSCOVER + "integrationtests.vstestcoveragedir";
    private static final String MSCOVER_INTEGRATION_TOOL="integrationtests.coveragetool";
    public static final String MSCOVER_UNIT_RESULTS= MSCOVER + "unittests.results";
    public static final String MSCOVER_MODE = MSCOVER + "mode";
    public static final String MSCOVER_UNITTEST_ASSEMBLIES = MSCOVER + "unittests.assemblies";
    public static final String MSCOVER_UNITTEST_HINTPATH = MSCOVER + "unittests.hintpath";
    public static final String MSCOVER_TESTSETTINGS = MSCOVER + "vstest.testsettings";
    public static final String MSCOVER_COVERAGETOOL = MSCOVER + "coveragetool";
    public static final String MSCOVER_IGNOREMISSING_DLL = MSCOVER + "vstest.ignoremissingdlls";
    public static final String MSCOVER_IGNOREMISSING_PDB = MSCOVER + "opencover.ignoremissingpdbs";
    private static final String MSCOVER_VSTEST_INSTALLDIR=MSCOVER+"vstest.installDirectory";
    private static final String MSCOVER_WORKSPACE_ROOT= MSCOVER + "workspace";
    
    private static final String MSCOVER_UNITTEST_PATTERN = MSCOVER + "unittests.projectpattern";
    
    /**
     * constructor for IOC
     * @param settings
     */
    @SuppressWarnings("ucd")
    public DefaultMsCoverConfiguration(Settings settings) {
        this.settings = settings;
        this.settingsHelper=new SettingsHelper(settings);
    }
    
    /**
     * Testing purposes
     * @param settings
     * @param settingsHelper
     */
    public DefaultMsCoverConfiguration(Settings settings, SettingsHelper settingsHelper) {
        this.settings=settings;
        this.settingsHelper = settingsHelper;
    }
    
    /**
     * Preferred constructor
     */
    public static MsCoverConfiguration create(Settings settings) {
        return new DefaultMsCoverConfiguration(settings);
    }


    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getUnitTestCoveragePath()
     */
    @Override
    public String getUnitTestCoveragePath() {
        return settings.getString(MSCOVER_UNIT_COVERAGEXML_PATH);
    }


    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getMode()
     */
    @Override
    public String getMode() {
        return settings.getString(MSCOVER_MODE);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getUnitTestsAssemblies()
     */
    @Override
    public String getUnitTestsAssemblies() {
        return settings.getString(MSCOVER_UNITTEST_ASSEMBLIES);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getTestSettings()
     */
    @Override
    public String getTestSettings() {
        return settings.getString(MSCOVER_TESTSETTINGS);
    }



   
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getLanguages()
     */
    @Override
    public List<String> getLanguages() {
        List<String> languages = new ArrayList<String>();
        String[] languageSetting = settings.getStringArrayBySeparator(
                "sonar.language", ",");
        if (languageSetting != null) {
            for (String language : languageSetting) {
                languages.add(language);
            }
        }

        return languages;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getRunMode()
     */
    @Override
    public RunMode getRunMode() {
        String name=settings.getString(MSCOVER_MODE);
        if(StringUtils.isEmpty(name)) {
            return RunMode.SKIP;
        }
        RunMode runMode = RunMode.SKIP;
        try {
            runMode= Enum.valueOf(RunMode.class, name.toUpperCase());
        } catch (IllegalArgumentException e) {
            String msg = "Invalid property value " + MSCOVER_MODE +"=" + name;
            LOG.error(msg);
            throw new IllegalStateException(msg,e);
        }
        return runMode;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#runOpenCover()
     */
    @Override
    public boolean runOpenCover() {      
        return getRunMode().equals(RunMode.RUNVSTEST) && "opencover".equalsIgnoreCase(settings.getString(MSCOVER_COVERAGETOOL));
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#runVsTest()
     */
    @Override
    public boolean runVsTest() {   
        return getRunMode().equals(RunMode.RUNVSTEST) && "vstest".equalsIgnoreCase(settings.getString(MSCOVER_COVERAGETOOL));
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getRequiredBuildConfiguration()
     */
    @Override
    public String getRequiredBuildConfiguration() {
        return getRequiredProperty("sonar.dotnet.buildConfiguration");
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getRequiredBuildPlatform()
     */
    @Override
    public String getRequiredBuildPlatform() {
        return getRequiredProperty("sonar.dotnet.buildPlatform");
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getUnitTestAssembliesThatCanBeIgnoredIfMissing()
     */
    @Override
    public Collection<String> getUnitTestAssembliesThatCanBeIgnoredIfMissing() {
        return getCollection(MSCOVER_IGNOREMISSING_DLL);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getPdbsThatMayBeIgnoredWhenMissing()
     */
    @Override
    public Collection<String> getPdbsThatMayBeIgnoredWhenMissing() {
        return getCollection(MSCOVER_IGNOREMISSING_PDB);
    }
    
    private Collection<String> getCollection(String name) {
        String[] values=settings.getStringArrayBySeparator(name, ",");
        Collection<String> collection = new ArrayList<String>() ;
        for(String value:values) {
            collection.add(value);
        }
        return collection;        
    }
    
    private String getRequiredProperty(String property) {
        String value = settings.getString(property);
        if(StringUtils.isEmpty(value)) {
            throw new IllegalStateException("missing property setting for :" + property);
        }
        return value;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#isIgnoreMissingUnitTestAssembliesSpecified()
     */
    @Override
    public boolean isIgnoreMissingUnitTestAssembliesSpecified() {
        return getUnitTestAssembliesThatCanBeIgnoredIfMissing().size()>0;
    }

    
    @Override
    public String getVsTestInstallPath() {
        return settings.getString(MSCOVER_VSTEST_INSTALLDIR);
    }

    @Override
    public String getUnitTestHintPath() {
        return settings.getString(MSCOVER_UNITTEST_HINTPATH);
    }
    

    @Override
    public String getIntegrationTestsDir() {
        String coverageDir=settings.getString(MSCOVER_INTEGRATION_VSTESTDIR);
        return coverageDir;
    }
    

    
    public static Collection<PropertyDefinition> getProperties() {
        Collection<PropertyDefinition> properties = new ArrayList<>();
        properties.add(createVsTestProperty(MSCOVER_VSTEST_INSTALLDIR,PropertyType.STRING)
                .name("vstest installdir")
                .description("path to directory where vstest.console.exe is installed")
                .defaultValue("C:/Program Files (x86)/Microsoft Visual Studio 12.0/Common7/IDE/CommonExtensions/Microsoft/TestWindow")
                .index(0)
                .build());
        properties.add(createIntegrationTestProperty(MSCOVER_INTEGRATION_VSTESTDIR,PropertyType.STRING)
                .name("vstest coverage dir")
                .description("path to directory vstest generated integration test coverage files")
                .index(0)
                .onQualifiers(Qualifiers.PROJECT)
                .build());
        properties.add(createIntegrationTestProperty(MSCOVER_WORKSPACE_ROOT,PropertyType.STRING)
                .name("workspace root dir")
                .description("root directory of your build")
                .index(1)
                .build());
        properties.add(createIntegrationTestProperty(MSCOVER_INTEGRATION_TOOL,PropertyType.SINGLE_SELECT_LIST)
        		.name("coverage tool")
        		.description("tool that will provide coverage information (opencover or vstest)")
        		.index(2)
        		.defaultValue("vstest")
        		.options("vstest", "opencover")
        		.build());
        
        return properties;

    }

    private static Builder createIntegrationTestProperty(String key, PropertyType propertyType) {
        return PropertyDefinition.builder(key).type(propertyType).subCategory("integration test");

    }
    private static Builder createVsTestProperty(String key, PropertyType propertyType) {
        return PropertyDefinition.builder(key).type(propertyType).subCategory("vstest");

    }

    @Override
    public File getWorkSpaceRoot() {
        String root=settings.getString(MSCOVER_WORKSPACE_ROOT);
        if(StringUtils.isEmpty(root)) {
            throw new IllegalStateException("undefined property :" + MSCOVER_WORKSPACE_ROOT);
        }
        File rootDir=new File(root);
        if(!rootDir.exists()) {
            throw new IllegalStateException("property :" + MSCOVER_WORKSPACE_ROOT + "=" + root + " does not exist");
        }
        return rootDir;
    }

    @Override
    public Pattern getTestProjectPattern() {
        Pattern pattern = settingsHelper.getPattern(MSCOVER_UNITTEST_PATTERN);
        return pattern;
        
    }
    
}
