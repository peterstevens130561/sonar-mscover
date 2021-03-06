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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sonar.api.utils.command.Command;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import com.stevpet.sonar.plugins.common.api.ShellCommand;

public class OpenCoverCommand implements ShellCommand {

    private  String path;

    private Map<String,String> arguments = new HashMap<String, String>();
    
    @SuppressWarnings("ucd")
    public OpenCoverCommand() {
    }
    
    public void setInstallDir(String path) {
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

    public void setHideSkipped(String skip) {
        addArgument("hideskipped",skip);
    }
    public void setExcludeFromCodeCoverageAttributeFilter() {
        addArgument("excludebyattribute","*.ExcludeFromCoverageAttribute*");
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
        Preconditions.checkNotNull(path,"installdir not set");
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

    @Override
    public String getExecutable() {
        return "OpenCover";
    }
}
