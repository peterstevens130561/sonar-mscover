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

/**
 * Contains settings directly related to opencover itself, i.e. the directory where OpenCover.Console.Exe is installed, command line arguments etc
 * These are most likely set in the sonar-runner.properties, or at global level
 * @author stevpet
 *
 */
public interface OpenCoverCommandLineConfiguration {
    /**
     * The directory where OpenCover.Console.Exe is installed. 
     * @throws IllegalArgumentValueException when value is not set
     * @return 
     */
    String getInstallDir();
    
    /**
     * Skip auto generated properties (yes/no)
     * @return
     */
    boolean getSkipAutoProps();
    
    /**
     * Used to set the -register:[value] option
     * <br>
     * - null - argument not set
     * <br>
     *  -any string value will lead to -user:[value]
     *  @throws IllegalArgumentValueException when value is not one of user, Path32, Path64
     * @return value of the setting, can be null
     */
    String getRegister();
}
