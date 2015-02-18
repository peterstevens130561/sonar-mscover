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

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;

public class InjectedMeasureSaver implements BatchExtension, MeasureSaver {

    private MeasureSaver measureSaver;
    public InjectedMeasureSaver(ResourceMediator resourceMediator) {
        measureSaver =SonarMeasureSaver.create(resourceMediator);
    }
    @Override
    public void setFile(File file) {
        measureSaver.setFile(file);

    }

    @Override
    public void saveFileMeasure(Measure measure) {
        measureSaver.saveFileMeasure(measure);
    }

    @Override
    public void saveFileMeasure(Metric metric, double value) {
        measureSaver.saveFileMeasure(metric, value);
    }

    @Override
    public void saveSummaryMeasure(Metric tests, double executedTests) {
        measureSaver.saveSummaryMeasure(tests, executedTests);
    }

    @Override
    public void setIgnoreTwiceSameMeasure() {
        measureSaver.setIgnoreTwiceSameMeasure();
    }

    @Override
    public void setExceptionOnTwiceSameMeasure() {
        measureSaver.setExceptionOnTwiceSameMeasure();
    }
    @Override
    public void setProjectAndContext(Project project,
            SensorContext sensorContext) {
        measureSaver.setProjectAndContext(project, sensorContext);
    }

}
