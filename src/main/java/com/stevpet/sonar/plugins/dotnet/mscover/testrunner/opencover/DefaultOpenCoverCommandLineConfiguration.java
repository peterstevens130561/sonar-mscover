package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;

public class DefaultOpenCoverCommandLineConfiguration implements OpenCoverCommandLineConfiguration {
    private static final String OPENCOVER = "sonar.mscover.opencover.";
    public static final String INSTALLDIR_KEY = OPENCOVER + "installDirectory";
    public static final String SKIPAUTOPROPS_KEY = OPENCOVER + "skipautoprops";
    public static final String REGISTER = OPENCOVER + "register";
    private final Settings settings;

    public DefaultOpenCoverCommandLineConfiguration(
            Settings settings) {
        this.settings = settings;
    }

    @Override
    public String getInstallDir() {
        String installDir = settings.getString(INSTALLDIR_KEY);
        if (StringUtils.isEmpty(installDir)) {
            throw new IllegalArgumentException("property " + INSTALLDIR_KEY + " not set");
        }
        return installDir;
    }

    @Override
    public boolean getSkipAutoProps() {
        boolean skip = settings.getBoolean(SKIPAUTOPROPS_KEY);
        return skip;
    }

    @Override
    public String getRegister() {
        String register = settings.getString(REGISTER);
        return register;
    }

}
