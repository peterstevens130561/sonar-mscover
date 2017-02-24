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
package com.stevpet.sonar.plugins.common.commandexecutor;

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

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.utils.System2;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @since 2.7
 */
public class DefaultCommand {
  private final String executable;
  private final List<String> arguments = Lists.newArrayList();
  private final List<String> argumentsForLogs = Lists.newArrayList();
  private final Map<String, String> env;
  private File directory;
  private boolean newShell = false;
  private final System2 system;

  DefaultCommand(String executable, System2 system) {
    Preconditions.checkArgument(!StringUtils.isBlank(executable), "DefaultCommand executable can not be blank");
    this.executable = executable;
    this.env = new HashMap<String,String>(system.envVariables());
    this.system = system;
  }

  /**
   * Create a command line without any arguments
   *
   * @param executable
   */
  public static DefaultCommand create(String executable) {
    return new DefaultCommand(executable, System2.INSTANCE);
  }

  public String getExecutable() {
    return executable;
  }

  public List<String> getArguments() {
    return ImmutableList.copyOf(arguments);
  }

  public DefaultCommand addArgument(String arg) {
    arguments.add(arg);
    argumentsForLogs.add(arg);
    return this;
  }

  public DefaultCommand addMaskedArgument(String arg) {
    arguments.add(arg);
    argumentsForLogs.add("********");
    return this;
  }

  public DefaultCommand addArguments(List<String> args) {
    arguments.addAll(args);
    argumentsForLogs.addAll(args);
    return this;
  }

  public DefaultCommand addArguments(String[] args) {
    Collections.addAll(arguments, args);
    Collections.addAll(argumentsForLogs, args);
    return this;
  }

  public File getDirectory() {
    return directory;
  }

  /**
   * Sets working directory.
   */
  public DefaultCommand setDirectory(File d) {
    this.directory = d;
    return this;
  }

  /**
   * @see org.sonar.api.utils.command.Command#getEnvironmentVariables()
   * @since 3.2
   */
  public DefaultCommand setEnvironmentVariable(String name, String value) {
    this.env.put(name, value);
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
    return ImmutableMap.copyOf(env);
  }

  /**
   * <code>true</code> if a new shell should be used to execute the command.
   * The default behavior is to not use a new shell.
   *
   * @since 3.3
   */
  public boolean isNewShell() {
    return newShell;
  }

  /**
   * Set to <code>true</code> if a new shell should be used to execute the command.
   * This is useful when the executed command is a script with no execution rights (+x on unix).
   *
   * On windows, the command will be executed with <code>cmd /C executable</code>.
   * On other platforms, the command will be executed with <code>sh executable</code>.
   *
   * @since 3.3
   */
  public DefaultCommand setNewShell(boolean b) {
    this.newShell = b;
    return this;
  }

  List<String> toStrings(boolean forLogs) {
    ImmutableList.Builder<String> command = ImmutableList.builder();
    if (newShell) {
      if (system.isOsWindows()) {
        command.add("cmd", "/C", "call");
      } else {
        command.add("sh");
      }
    }
    command.add(executable);
    command.addAll(forLogs ? argumentsForLogs : arguments);
    return command.build();
  }

  public String toCommandLine() {
    return Joiner.on(" ").join(toStrings(false));
  }

  @Override
  public String toString() {
    return Joiner.on(" ").join(toStrings(true));
  }
}
