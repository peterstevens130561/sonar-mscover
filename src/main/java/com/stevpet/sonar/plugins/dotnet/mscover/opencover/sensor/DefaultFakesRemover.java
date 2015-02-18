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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;
import java.io.FilenameFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultFakesRemover implements FakesRemover {

    FilenameFilter fakesFilter = new FakesFilter();
    private static Logger LOG = LoggerFactory.getLogger(DefaultFakesRemover.class);
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.RemoveFakes#removeFakes(java.io.File)
     */
    public int removeFakes(File myDir) {

        if(myDir==null) {
            return 0;
        }
        File [] files = myDir.listFiles(fakesFilter);
        if(files==null) {
            return 0;
        }
        int removed=0;
        for (File file : files) {
            LOG.warn("Removing fakes file {}",file.getName());
            file.delete();
            removed++;
        }
        return removed;
    }
    

}
