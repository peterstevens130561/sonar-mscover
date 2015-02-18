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


import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.File;


public class FileResource implements ResourceSeam {
    private SensorContext sensorContext;
    private File resource;
    private boolean isExcluded = false;
    FileResource(SensorContext sensorContext,File file) {
        if(file==null || sensorContext==null) {
            throw new IllegalArgumentException("Programmer error: FileResource invoked with null argument");
        }
        this.resource=file;
        this.sensorContext=sensorContext;
    }

    public void saveMetricValue(Metric metric,
            double value) {
        sensorContext.saveMeasure(resource,metric,value);
        
    }
    public void saveMeasure(Measure measure) {
        sensorContext.saveMeasure(resource,measure);
    }
    

    public String getLongName() {
        return resource.getLongName();
    }

    public org.sonar.api.resources.File getResource() {
        return resource;
    }
    
    public boolean isIncluded() {
        return !isExcluded;
    }

   
    public void setIsExcluded() {
        isExcluded=true;
    }

}
