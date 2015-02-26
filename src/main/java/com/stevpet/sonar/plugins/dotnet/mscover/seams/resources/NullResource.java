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
package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;

public class NullResource implements ResourceSeam {

    public void saveMetricValue(Metric metric, double value) {
        //Mull resource, so ignore
    }

    public void saveMeasure(Measure measure) { 
        // Null resource, so ignore
    }

    /**
     * isIndex may be used to load the file, which we want to prevent, so
     * always return true
     */
    public boolean isIndexed(boolean acceptExcluded) {
        return true;
    }

    public String getLongName() {
        return StringUtils.EMPTY;
    }


    public boolean isIncluded() {
        return false;
    }



    public void setIsExcluded() {
        // Null resource so ignore
    }

    public void readSource(java.io.File file, String path, Charset charset) {
        // Null resource so ignore
    }
}
