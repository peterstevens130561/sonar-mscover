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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.command;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.sonar.api.utils.command.Command;

import com.stevpet.sonar.plugins.common.api.ShellCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverTarget;

/**
 * Implementation of VsTest
 * 
 * See {@link commandline msdn.microsoft.com/en-us/library/jj155796.aspx}
 */
public class VSTestCommand implements ShellCommand,OpenCoverTarget {

    private static String defaultPath = "C:/Program Files (x86)/Microsoft Visual Studio 11.0/" +
            "Common7/IDE/CommonExtensions/Microsoft/TestWindow/vstest.console.exe";
    private String commandPath;
    private String testSettingsPath;
    private List<String> unitTestAssemblyPaths;
    private boolean doCodeCoverage;
    private String platform;
    private String assembliesDir;
	private String testCaseFilter;
    
    public VSTestCommand() {
        commandPath=defaultPath;
        
    }
    public static VSTestCommand create() {
        return new VSTestCommand();
    }
    
    public void setExecutableDir(String path) {
    	commandPath=path + "\\vstest.console.exe";
    }
    
    public void setTestCaseFilter(String testCaseFilter) {
    		this.testCaseFilter = testCaseFilter;
    }
    /**
     * @return Creates the commandline with all options
     */
    public String toCommandLine() {
        Command command = toCommand();
        return command.toCommandLine();
    }

    public Command toCommand() {
        Command command = Command.create(commandPath);

        command.addArguments(unitTestAssemblyPaths);
        command.addArgument("/Settings:" + testSettingsPath);
        if (doCodeCoverage) {
            command.addArgument("/EnableCodeCoverage");
        }
        command.addArgument("/Logger:trx");
        addPlatformIfSpecified(command);
        if(!StringUtils.isEmpty(testCaseFilter)) {
        	command.addArgument("/TestCaseFilter:\"" + testCaseFilter + "\"");
        }
        return command;
    }
    
    private void addPlatformIfSpecified(Command command) {
        if(StringUtils.isEmpty(platform)) {
            return;
        }
        platform=platform.replaceAll("\\s", "");
        if("anycpu".equalsIgnoreCase(platform)) {
            platform="x64";
        }
        command.addArgument("/Platform:" + platform);
        if("x64".equals(platform)) {
            command.addArgument("/inIsolation");
        }
    }
    
    public String getTestSettingsPath() {
        return testSettingsPath;
    }
    public void setTestSettingsFile(File testSettingsFile) {
        this.testSettingsPath = testSettingsFile.getAbsolutePath();
    }
    
    public void setUnitTestAssembliesDir(String assembliesDir) {
        this.assembliesDir = assembliesDir;
    }
    public void setUnitTestAssembliesPath(List<String> unitTestAssemblyPaths) {

        this.unitTestAssemblyPaths = CommandHelper.parenthesizeArguments(unitTestAssemblyPaths);
    }
    
    public String getExecutable() {
        return commandPath;
    }
    public String getArguments() {
        Command command = toCommand();
        String commandLine=command.toCommandLine();
        int start = commandPath.length() + 1;
        return commandLine.substring(start);
    }
    public void setCodeCoverage(boolean doCodeCoverage) {
        this.doCodeCoverage=doCodeCoverage;
    }
    public void setPlatform(String platform) {
        this.platform=platform;
        
    }
    public String getAssembliesDir() {
        return assembliesDir;
    }

    @Override
    public String toString() {
    	return toCommandLine();
    }

}
