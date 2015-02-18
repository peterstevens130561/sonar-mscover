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
package com.stevpet.sonar.plugins.dotnet.mscover.seams;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.sonar.api.utils.command.Command;

public class WindowsCommandSeam {
    private final Command command;
    private WindowsCommandSeam(String executable) {
        command = Command.create(executable);
        
    }

    public static WindowsCommandSeam create(String executable) {
        return new WindowsCommandSeam(executable);
    }
    
    public String getExecutable() {
        return command.getExecutable();
      }

      public List<String> getArguments() {
        return command.getArguments();
      }

      public WindowsCommandSeam addArgument(String arg) {
        command.addArgument(arg);
        return this;
      }

      public WindowsCommandSeam addArguments(List<String> args) {
        command.addArguments(args);
        return this;
      }

      public WindowsCommandSeam addArguments(String[] args) {
        command.addArguments(args);
        return this;
      }

      public File getDirectory() {
        return command.getDirectory();
      }

      /**
       * Sets working directory.
       */
      public WindowsCommandSeam setDirectory(File d) {
        command.setDirectory(d);
        return this;
      }

      /**
       * @see org.sonar.api.utils.command.Command#getEnvironmentVariables()
       * @since 3.2
       */
      public WindowsCommandSeam setEnvironmentVariable(String name, String value) {
        command.setEnvironmentVariable(name, value);
        return this;
      }

      /**
       * Environment variables that are propagated during command execution.
       * The initial value is a copy of the environment of the current process.
       *
       * @return a non-null and immutable map of variables
       * @since 3.2
       */
      public Map<String, String> getEnvironmentVariables() {
        return command.getEnvironmentVariables();
      }

      boolean isNewShell() {
        return command.isNewShell();
      }

      public WindowsCommandSeam setNewShell(boolean b) {
        command.setNewShell(b);
        return this;
      }

      public String toCommandLine() {
        return command.toCommandLine();
      }

      @Override
      public String toString() {
        return toCommandLine();
      }

}
