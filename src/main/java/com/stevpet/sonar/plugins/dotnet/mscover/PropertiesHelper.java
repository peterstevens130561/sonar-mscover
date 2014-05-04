package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Language;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class PropertiesHelper {

    public enum RunMode {
        SKIP,
        REUSE,
        RUNVSTEST,
        RUNOPENCOVER
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
    public static final String MSCOVER_UNIT_RESULTS= MSCOVER + "unittests.results";
    public static final String MSCOVER_MODE = MSCOVER + "mode";
    public static final String MSCOVER_UNITTEST_ASSEMBLIES = MSCOVER + "unittests.assemblies";
    public static final String MSCOVER_TESTSETTINGS = MSCOVER + "vstest.testsettings";
    
    @Deprecated
    public PropertiesHelper(Settings settings) {
        this.settings = settings;
    }
    
    public boolean isIntegrationTestsEnabled() {
        return StringUtils.isNotEmpty(getIntegrationTestsPath());
    }
    
    public boolean isUnitTestsEnabled() {
        return StringUtils.isNotEmpty(getUnitTestCoveragePath());
    }
    public String getIntegrationTestsPath() {
        return settings.getString(MSCOVER_INTEGRATION_COVERAGEXML_PATH);
    }
    
    public boolean excuteRoot() {
        return settings.getBoolean(MSCOVER_EXECUTEROOT);
    }
    
    public String getUnitTestCoveragePath() {
        return settings.getString(MSCOVER_UNIT_COVERAGEXML_PATH);
    }

    public String getExclusions() {
        return settings.getString(MSCOVER_EXCLUSIONS);
    }
    
    public boolean isPluginEnabled() {
        return isUnitTestsEnabled() || isIntegrationTestsEnabled();
    }

    public String getInclusions() {
        return settings.getString(MSCOVER_INCLUSIONS);
    }
    
 
    public String getCutOffDate() {
        return settings.getString(MSCOVER_CUTOFFDATE);
    }

    public String getIntegrationTestResultsPath() {
        return settings.getString(MSCOVER_INTEGRATION_RESULTS);
    }
    
    public String getUnitTestResultsPath() {
        return settings.getString(MSCOVER_UNIT_RESULTS);
    }

    public String getMode() {
        // TODO Auto-generated method stub
        return settings.getString(MSCOVER_MODE);
    }
    
    public String getUnitTestsAssemblies() {
        return settings.getString(MSCOVER_UNITTEST_ASSEMBLIES);
    }

    public String getTestSettings() {
        return settings.getString(MSCOVER_TESTSETTINGS);
    }

    /**
     * Preferred constructor
     */
    public static PropertiesHelper create(Settings settings) {
        return new PropertiesHelper(settings);
    }

    public boolean shouldMsCoverRun() {
        String mode = getMode();
        return StringUtils.isNotEmpty(mode) && !"skip".equals(mode);
    }
    
    public boolean isCPlusPlus() {
        return getLanguages().contains("c++");
    }

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
    
    public RunMode getRunMode() {
        String name=settings.getString(MSCOVER_MODE);
        if(StringUtils.isEmpty(name)) {
            return RunMode.SKIP;
        }
        RunMode runMode = RunMode.SKIP;
        try {
            runMode= Enum.valueOf(RunMode.class, name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MsCoverException("Invalid property value " + MSCOVER_MODE +"=" + name);
        }
        return runMode;
    }
    
}
