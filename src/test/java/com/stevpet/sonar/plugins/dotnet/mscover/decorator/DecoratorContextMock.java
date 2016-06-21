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
package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Matchers;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class DecoratorContextMock extends GenericClassMock<DecoratorContext> {
    public DecoratorContextMock() {
        super(DecoratorContext.class);
    }

    /**
     * create the measure to return when getMeasure is invoked
     * @param metric of the measure
     * @param value of the measure
     */
    public void givenMeasure(Metric<?> metric, double value) {
        Measure<?> measure = new Measure<Object>(metric);
        measure.setValue(value);
        givenMeasure(metric,measure);
    }

    public void givenMeasure(Metric<?> metric, Measure<?> measure) {
        when(instance.getMeasure(metric)).thenReturn(measure);  
    }

    /**
     * simplistic verify that saveMesure is invoked exact count times, for any metric, any double
     * @param count
     */
    public void verifySaveMeasureInvoked(int count) {
        verify(instance,times(count)).saveMeasure(Matchers.any(Metric.class),Matchers.any(Double.class));     
    } 

}
