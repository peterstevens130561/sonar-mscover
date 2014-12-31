/*
 * SonarQube MSCover coverage plugin
 * Copyright (C) 2014 Peter Stevens
 * peter@famstevens.eu
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
package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

public abstract class AbstractBaseSensor implements Sensor {


    private static final Logger LOG = LoggerFactory.getLogger(AbstractBaseSensor.class);

    public static final String MODE_SKIP = "skip";
    public static final String MODE_REUSE_REPORT = "reuseReport";

    private final String toolName;
    private final String executionMode;

    /**
     * Creates an {@link AbstractDotNetSensor} that has a {@link MicrosoftWindowsEnvironment} reference.
     *
     * @param microsoftWindowsEnvironment
     *          the {@link MicrosoftWindowsEnvironment}
     */
    protected AbstractBaseSensor(String toolName, String executionMode) {
      this.toolName = toolName;
      this.executionMode = executionMode;
    }

    /**
     * {@inheritDoc}
     */
    public boolean shouldExecuteOnProject(Project project) {
      if (project.isRoot() || !isLanguageSupported(project.getLanguageKey())) {
        return false;
      }
      boolean skipMode = MODE_SKIP.equalsIgnoreCase(getExecutionMode());
      if (skipMode) {
        LOG.info("{} plugin won't execute as it is set to 'skip' mode.", getToolName());
        return false;
      }

      return true;
    }

    private boolean isLanguageSupported(String languageKey) {
      for (String key : getSupportedLanguages()) {
        if (key.equals(languageKey)) {
          return true;
        }
      }
      return false;
    }

    /**
     * Must return the list of supported languages.
     *
     * @return the keys of the supported languages.
     */
    public abstract String[] getSupportedLanguages();

    /**
     * {@inheritDoc}
     */
    public abstract void analyse(Project project, SensorContext context);

    /**
     * @return the toolName
     */
    protected String getToolName() {
      return toolName;
    }

    /**
     * @return the executionMode
     */
    protected String getExecutionMode() {
      return executionMode;
    }

    
}
