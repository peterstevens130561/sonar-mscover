/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.sonar.api.BatchExtension;
import org.sonar.api.PropertyType;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyFieldDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.resources.Qualifiers;

import com.stevpet.sonar.plugings.dotnet.resharper.ReSharperConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverRequiredPropertyMissingException;
@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class DefaultMsCoverConfiguration implements BatchExtension, MsCoverConfiguration  {

    public enum RunMode {
        SKIP,
        REUSE,
        RUNVSTEST,
        NULL
    }
    private final Settings settings;
    public static final String MSCOVER = "sonar.mscover.";
    public static final String MSCOVER_INTEGRATION_COVERAGEXML_PATH = MSCOVER + "integrationtests.coveragexml";
    public static final String MSCOVER_UNIT_COVERAGEXML_PATH=MSCOVER + "unittests.coveragexml";
    public static final String MSCOVER_EXECUTEROOT = MSCOVER + "rootproject";
    public static final String MSCOVER_EXCLUSIONS=MSCOVER + "exclusions";
    public static final String MSCOVER_INCLUSIONS=MSCOVER + "inclusions";
    public static final String MSCOVER_CUTOFFDATE=MSCOVER + "cutoffdate" ;
    public static final String MSCOVER_INTEGRATION_RESULTS= MSCOVER + "integrationtests.results";
    public static final String MSCOVER_INTEGRATION_VSTESTDIR= MSCOVER + "integrationtests.vstestcoveragedir";
    public static final String MSCOVER_UNIT_RESULTS= MSCOVER + "unittests.results";
    public static final String MSCOVER_MODE = MSCOVER + "mode";
    public static final String MSCOVER_UNITTEST_ASSEMBLIES = MSCOVER + "unittests.assemblies";
    public static final String MSCOVER_UNITTEST_HINTPATH = MSCOVER + "unittests.hintpath";
    public static final String MSCOVER_TESTSETTINGS = MSCOVER + "vstest.testsettings";
    public static final String MSCOVER_COVERAGETOOL = MSCOVER + "coveragetool";
    public static final String MSCOVER_IGNOREMISSING_DLL = MSCOVER + "vstest.ignoremissingdlls";
    public static final String MSCOVER_IGNOREMISSING_PDB = MSCOVER + "opencover.ignoremissingpdbs";
    public static final String MSCOVER_OPENCOVER_SKIPAUTOPROPS = MSCOVER + "opencover.skipautoprops";
    public static final String MSCOVER_VSTEST_INSTALLDIR=MSCOVER+"vstest.installDirectory";
    public static final String MSCOVER_WORKSPACE_ROOT= MSCOVER + "workspace";
    
    @SuppressWarnings("ucd")
    public DefaultMsCoverConfiguration(Settings settings) {
        this.settings = settings;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#isIntegrationTestsEnabled()
     */
    public boolean isIntegrationTestsEnabled() {
        return StringUtils.isNotEmpty(getIntegrationTestsPath()) || StringUtils.isNotEmpty(getIntegrationTestsDir());
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#isUnitTestsEnabled()
     */
    public boolean isUnitTestsEnabled() {
        return StringUtils.isNotEmpty(getUnitTestCoveragePath());
    }
    

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getIntegrationTestsPath()
     */
    public String getIntegrationTestsPath() {
        return settings.getString(MSCOVER_INTEGRATION_COVERAGEXML_PATH);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#excuteRoot()
     */
    public boolean excuteRoot() {
        return settings.getBoolean(MSCOVER_EXECUTEROOT);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getUnitTestCoveragePath()
     */
    public String getUnitTestCoveragePath() {
        return settings.getString(MSCOVER_UNIT_COVERAGEXML_PATH);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getExclusions()
     */
    public String getExclusions() {
        return settings.getString(MSCOVER_EXCLUSIONS);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#isPluginEnabled()
     */
    public boolean isPluginEnabled() {
        return isUnitTestsEnabled() || isIntegrationTestsEnabled();
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getInclusions()
     */
    public String getInclusions() {
        return settings.getString(MSCOVER_INCLUSIONS);
    }
    
 
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getCutOffDate()
     */
    public String getCutOffDate() {
        return settings.getString(MSCOVER_CUTOFFDATE);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getIntegrationTestResultsPath()
     */
    public String getIntegrationTestResultsPath() {
        return settings.getString(MSCOVER_INTEGRATION_RESULTS);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getUnitTestResultsPath()
     */
    public String getUnitTestResultsPath() {
        return settings.getString(MSCOVER_UNIT_RESULTS);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getMode()
     */
    public String getMode() {
        return settings.getString(MSCOVER_MODE);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getUnitTestsAssemblies()
     */
    public String getUnitTestsAssemblies() {
        return settings.getString(MSCOVER_UNITTEST_ASSEMBLIES);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getTestSettings()
     */
    public String getTestSettings() {
        return settings.getString(MSCOVER_TESTSETTINGS);
    }

    /**
     * Preferred constructor
     */
    public static MsCoverConfiguration create(Settings settings) {
        return new DefaultMsCoverConfiguration(settings);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#shouldMsCoverRun()
     */
    public boolean shouldMsCoverRun() {
        String mode = getMode();
        return StringUtils.isNotEmpty(mode) && !"skip".equals(mode);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#isCPlusPlus()
     */
    public boolean isCPlusPlus() {
        return getLanguages().contains("c++");
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getLanguages()
     */
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
            Log.error(msg);
            throw new MsCoverException(msg,e);
        }
        return runMode;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#runOpenCover()
     */
    public boolean runOpenCover() {      
        return getRunMode().equals(RunMode.RUNVSTEST) && "opencover".equalsIgnoreCase(settings.getString(MSCOVER_COVERAGETOOL));
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#runVsTest()
     */
    public boolean runVsTest() {   
        return getRunMode().equals(RunMode.RUNVSTEST) && "vstest".equalsIgnoreCase(settings.getString(MSCOVER_COVERAGETOOL));
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getRequiredBuildConfiguration()
     */
    public String getRequiredBuildConfiguration() {
        return getRequiredProperty("sonar.dotnet.buildConfiguration");
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getRequiredBuildPlatform()
     */
    public String getRequiredBuildPlatform() {
        return getRequiredProperty("sonar.dotnet.buildPlatform");
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getUnitTestAssembliesThatCanBeIgnoredIfMissing()
     */
    public Collection<String> getUnitTestAssembliesThatCanBeIgnoredIfMissing() {
        return getCollection(MSCOVER_IGNOREMISSING_DLL);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getPdbsThatMayBeIgnoredWhenMissing()
     */
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
            throw new MsCoverRequiredPropertyMissingException(property);
        }
        return value;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#isIgnoreMissingUnitTestAssembliesSpecified()
     */
    public boolean isIgnoreMissingUnitTestAssembliesSpecified() {
        return getUnitTestAssembliesThatCanBeIgnoredIfMissing().size()>0;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.PropertiesInterface#getOpenCoverInstallPath()
     */
    public String getOpenCoverInstallPath() {
        return settings.getString("sonar.opencover.installDirectory");
    }
    
    @Override
    public String getVsTestInstallPath() {
        return settings.getString(MSCOVER_VSTEST_INSTALLDIR);
    }

    public String getUnitTestHintPath() {
        return settings.getString(MSCOVER_UNITTEST_HINTPATH);
    }
    
    public boolean getOpenCoverSkipAutoProps() {
        return settings.getBoolean(MSCOVER_OPENCOVER_SKIPAUTOPROPS);
    }

    @Override
    public String getIntegrationTestsDir() {
        return settings.getString(MSCOVER_INTEGRATION_VSTESTDIR);
    }
    
    public static Collection<PropertyDefinition> getProperties() {
        Collection<PropertyDefinition> properties = new ArrayList<>();
        properties.add(createVsTestProperty(MSCOVER_VSTEST_INSTALLDIR,PropertyType.STRING)
                .name("vstest installdir")
                .description("path to directory where vstest.console.exe is installed")
                .defaultValue("C:/Program Files (x86)/Microsoft Visual Studio 12.0/Common7/IDE/CommonExtensions/Microsoft/TestWindow")
                .index(0)
                .build());

        return properties;

    }

    private static Builder createVsTestProperty(String key, PropertyType propertyType) {
        return PropertyDefinition.builder(key).type(propertyType).subCategory("vstest");

    }

    @Override
    public File getWorkSpaceRoot() {
        String root=settings.getString(MSCOVER_WORKSPACE_ROOT);
        if(StringUtils.isEmpty(root)) {
            throw new MsCoverException("undefined property :" + MSCOVER_WORKSPACE_ROOT);
        }
        File rootDir=new File(root);
        if(!rootDir.exists()) {
            throw new MsCoverException("property :" + MSCOVER_WORKSPACE_ROOT + "=" + root + " does not exist");
        }
        return rootDir;
    }
    
}
