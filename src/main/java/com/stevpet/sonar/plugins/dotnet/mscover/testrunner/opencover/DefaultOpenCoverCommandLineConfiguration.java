package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;

public class DefaultOpenCoverCommandLineConfiguration implements OpenCoverCommandLineConfiguration {
    private static final String OPENCOVER = "sonar.mscover.opencover.";
    private static String INSTALLDIR_KEY = OPENCOVER + "installDirectory";
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getRegister() {
        // TODO Auto-generated method stub
        return null;
    }

}
