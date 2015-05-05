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
package com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter;

import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ShellCommand;

public abstract class CodeCoverageCommand implements ShellCommand{

    public abstract void setCoveragePath(String path);

    public abstract void setOutputPath(String path);

    /**
     * 
     * 
     * set the path to the .sonar folder
     * 
     * @param path
     */
    public abstract void setSonarPath(String path);

    /**
     * install the binary from resource by first removing a potential old one, then
     * taking it from the resource jar and put it in the sonar working directory
     */
    public abstract void install();

}