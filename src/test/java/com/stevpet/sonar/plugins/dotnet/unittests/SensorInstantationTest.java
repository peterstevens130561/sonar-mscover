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
package com.stevpet.sonar.plugins.dotnet.unittests;

import org.junit.Before;
import org.mockito.Mock;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.SonarPlugin;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.plugin.MsCoverPlugin;

public class SensorInstantationTest {

	@Mock Settings settings ;
	@Mock Project project;
	DefaultPicoContainer picoContainer;
	
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		picoContainer = new DefaultPicoContainer() ;
		picoContainer.addComponent( settings);
		SonarPlugin plugin = new MsCoverPlugin();
		for(Object clazz : plugin.getExtensions()) {
			picoContainer.addComponent(clazz);
		}
		
	}
	
	public void instantiate() {
		picoContainer.addComponent(DefaultFileSystem.class).addComponent(project);
		picoContainer.getComponent(Sensor.class);		
	}
}
