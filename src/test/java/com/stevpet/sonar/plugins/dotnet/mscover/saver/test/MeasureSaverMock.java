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
package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class MeasureSaverMock extends GenericClassMock<MeasureSaver> {

    public MeasureSaverMock() {
        super(MeasureSaver.class);
    }

    public void verifySaveProjectSummary(Metric tests, double value) {
        verify(instance,times(1)).saveSummaryMeasure(tests, value);
    }

    public void verifySaveFileMeasure(int frequency,Measure measure) {
        verify(instance,times(frequency)).saveFileMeasure(measure);
    }

    public void verifySaveFileMeasure(Metric metric, double value) {
        verify(instance,times(1)).saveFileMeasure(eq(metric), eq(value));
    }
    public void verifyInvokedSaveFileMeasure(int i, Metric metric) {
        verify(instance,times(i)).saveFileMeasure(eq(metric), anyDouble());
    }

    public void verifySaveFileMeasure(Measure lineHits) {
        verify(instance,times(1)).saveFileMeasure(eq(lineHits));        
    }
    

}
