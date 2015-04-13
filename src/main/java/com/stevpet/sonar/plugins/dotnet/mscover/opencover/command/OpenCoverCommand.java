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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.command;

import java.util.List;
import java.util.Map;

import org.sonar.api.utils.command.Command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ShellCommand;

public class OpenCoverCommand implements ShellCommand {

    private  String path;

    Map<String,String> arguments = Maps.newHashMap();
    
    public OpenCoverCommand(MsCoverProperties msCoverProperties) {
        path=msCoverProperties.getOpenCoverInstallPath();
    }
    
    public OpenCoverCommand() {
        
    }
    
    public OpenCoverCommand(String path) {
        this.path=path;

    }
    
    public void setCommandPath(String path) {
        this.path=path;
        
    }
    public void setRegister(String value) {
        addArgument("register",value);
    }
    
    public void setTargetDir(String value) {
        addPathArgument("targetdir",value);
    }
    
    public void setTargetCommand(OpenCoverTarget openCoverTarget) {
        setTarget(openCoverTarget.getExecutable());
        setTargetArgs(openCoverTarget.getArguments());
    }
    
    private void setTarget(String value) {
        addSpacedArgument("target",value);
    }
    
    public void setSkipAutoProps() {
       addOption("skipautoprops");
    }
    private void setTargetArgs( String value) {
        String escapedArg=value.replaceAll("\\\"", "\\\\\"");
        addSpacedArgument("targetargs",escapedArg );
    }
    
    public void setMergeByHash() {
        addArgument("mergebyhash","");
    }
    
    public void setFilter(String value) {
        addSpacedArgument("filter",value);
    }
    
    public void setExcludeByFileFilter(List<String> filters) {
        addFileFilterArgument("excludebyfile",filters);
        
    }

    public void setExcludeFromCodeCoverageAttributeFilter() {
        addArgument("excludebyattribute","*ExcludeFromCodeCoverage*");
    }

    public void setOutputPath(String value) {
        addPathArgument("output",value);
    }
    
    private void addPathArgument(String name, String value) {
        String pathWithForwardSlashesOnly=value.replaceAll("\\\\", "/");
        addSpacedArgument(name,pathWithForwardSlashesOnly);
    }

    public String toCommandLine() {
        return toCommand().toString();
    }

    public Command toCommand() {
        List<String> list = Lists.newArrayList(arguments.values());
        Command command =Command.create(path + "/OpenCover.Console.Exe");
        command.addArguments(list);
        return command ;
    }

    private void addSpacedArgument(String name, String valueWithSpaces) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"-").append(name).append(":").append(valueWithSpaces).append("\"");
        arguments.put(name, sb.toString());
    }
    
    private void addArgument(String name, String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("-").append(name).append(":").append(value);
        arguments.put(name, sb.toString());
    }
    
    private void addOption(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("-").append(name);
        arguments.put(name, sb.toString());
    }
    
    private void addFileFilterArgument(String option, List<String> filters) {
        if(filters==null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(String filter: filters) {
            sb.append(filter).append(";");
        }
        String filterArgument = sb.toString().replaceAll(";$","");
        addArgument(option,filterArgument);
    }
    
    @Override
    public String toString() {
    	return toCommandLine();
    }
}
