/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
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
