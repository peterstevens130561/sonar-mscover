/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
 */
package com.stevpet.sonar.plugins.dotnet.unittests;

import org.junit.Before;
import org.mockito.Mock;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeVersion;
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
		Plugin plugin = new MsCoverPlugin();
		Plugin.Context context = new Plugin.Context(SonarQubeVersion.V5_6);
		plugin.define(context);
		for(Object clazz : context.getExtensions()) {
			picoContainer.addComponent(clazz);
		}
		
	}
	
	public void instantiate() {
		picoContainer.addComponent(DefaultFileSystem.class).addComponent(project);
		picoContainer.getComponent(Sensor.class);		
	}
}
