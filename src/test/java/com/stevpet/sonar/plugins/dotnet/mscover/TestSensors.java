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
package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.reflections.Reflections;
import org.sonar.api.batch.Sensor;

import com.stevpet.sonar.plugins.dotnet.mscover.plugin.Extension;

@Extension
public class TestSensors {

    public void simpleExperiment() {
        List<Class<?>> results = new ArrayList<Class<?>>();
        Reflections reflections = new Reflections("com.stevpet.sonar.plugins.dotnet.mscover");
         Set<Class<? extends Sensor>> subtypes=reflections.getSubTypesOf(Sensor.class);
        for(Class<? extends Sensor> clazz : subtypes) {
        
            Extension annotation = clazz.getAnnotation(Extension.class);
            if(annotation !=null) {
                results.add(clazz);
            }
        }
        Assert.assertNotNull(subtypes);
        Assert.assertEquals(3,results.size());
    }
    

    public void straightfromClass() {
           Extension extension=this.getClass().getAnnotation(Extension.class);
           Assert.assertNotNull(extension);
    }
}