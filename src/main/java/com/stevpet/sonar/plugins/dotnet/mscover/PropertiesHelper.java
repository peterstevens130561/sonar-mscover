package com.stevpet.sonar.plugins.dotnet.mscover;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;

public class PropertiesHelper {

    private final Settings settings;
    public static final String MSCOVER = "sonar.mscover.";
    public static final String MSCOVER_INTEGRATION_PATH = MSCOVER + "integrationtests";
    public static final String MSCOVER_UNIT_PATH=MSCOVER + "unittests";
    public static final String MSCOVER_EXECUTEROOT = MSCOVER + "rootproject";
    public static final String MSCOVER_EXCLUSIONS=MSCOVER + "exclusions";
    public static final String MSCOVER_INCLUSIONS=MSCOVER + "inclusions";
    public static final String MSCOVER_CUTOFFDATE=MSCOVER + "cutoffdate" ;
    
    public PropertiesHelper(Settings settings) {
        this.settings = settings;
    }
    
    public boolean isIntegrationTestsEnabled() {
        return StringUtils.isNotEmpty(getIntegrationTestsPath());
    }
    
    public boolean isUnitTestsEnabled() {
        return StringUtils.isNotEmpty(getUnitTestsPath());
    }
    public String getIntegrationTestsPath() {
        return settings.getString(MSCOVER_INTEGRATION_PATH);
    }
    
    public boolean excuteRoot() {
        return settings.getBoolean(MSCOVER_EXECUTEROOT);
    }
    
    public String getUnitTestsPath() {
        return settings.getString(MSCOVER_UNIT_PATH);
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
}
