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
package com.stevpet.sonar.plugins.dotnet.mscover.sonarseams;

import java.io.File;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;


/**
 * Use as seam between the app and sonar to save any measure. Allows better testing of the
 * saving, as you can intercept the saving with your own implementation
 * @author stevpet
 *
 */
public interface MeasureSaver {

    /**
     * set the resource file on which we'll set the measure. The resourcemediator will attempt
     * to translate it to a resource.
     * @param file
     */
    public void setFile(File file);

    /**
     * if the file is to be included in the analysis then the measure will be saved
     * @param measure
     */
    public void saveFileMeasure(Measure measure);

    public void saveFileMeasure(Metric metric, double value);

    public void saveSummaryMeasure(Metric tests, double executedTests);

    public void setIgnoreTwiceSameMeasure();

    public void setExceptionOnTwiceSameMeasure();

    public void setProjectAndContext(Project project,
            SensorContext sensorContext);

}