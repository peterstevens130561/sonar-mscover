/*
 * Sonar .NET Plugin :: ReSharper
 * Copyright (C) 2013 John M. Wright
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
package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import java.io.File;
import java.util.List;

/**
 * Collects the ReSharper reporting into sonar.
 */
public class CPlusPlusPreprocessorSensor implements Sensor {

  
    private Settings settings;
    private FileSystem fileSystem;

    /**
     * Constructs a {@link org.sonar.plugins.csharp.resharper.ReSharperSensor}.
     */
    public CPlusPlusPreprocessorSensor(FileSystem fileSystem,
            Settings settings) {
        this.fileSystem = fileSystem;
        this.settings = settings;

    }

    /**
     * {@inheritDoc}
     */
    public void analyse(Project project, SensorContext context) {
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        boolean hasCs = fileSystem.languages().contains("c++");
        boolean isRoot = project.isRoot();
        return hasCs && isRoot;
    }

}
