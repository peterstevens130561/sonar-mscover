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
package com.stevpet.sonar.plugins.dotnet.resharper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

/**
 * Collects the ReSharper reporting into sonar.
 */
public class ReSharperSensor implements Sensor {
    private Logger LOG = LoggerFactory.getLogger(ReSharperSensor.class);
    private FileSystem fileSystem;

    private Settings settings;

    private ResharperWorkflow resharperWorkflow;

    private ReSharperConfiguration configuration;


    /**
     * Constructs a {@link org.sonar.plugins.csharp.resharper.ReSharperSensor}.
     */
    public ReSharperSensor(
            FileSystem fileSystem,
            Settings settings,
            ResharperWorkflow resharperWorkflow,
            ReSharperConfiguration configuration) {
        this.resharperWorkflow = resharperWorkflow;
        this.configuration = configuration;
        this.fileSystem = fileSystem;
        this.settings = settings;
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        boolean hasCs = fileSystem.languages().contains("cs");
        boolean skip = ReSharperConfiguration.MODE_SKIP.equalsIgnoreCase(settings.getString(ReSharperConfiguration.MODE));
        boolean isRoot = project.isRoot();
        return hasCs && !skip && !isRoot;
    }

    @Override
    public void analyse(Project module, SensorContext context) {
        try {
            resharperWorkflow.executeModule(module);
        } catch ( Exception e ) {
            if(configuration.failOnException()) {
                LOG.error(e.getMessage());
                throw e;
            }
        }
    }

}
