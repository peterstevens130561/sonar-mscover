package com.stevpet.sonar.plugins.dotnet.mscover.opencover.command;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sonar.api.utils.command.Command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ShellCommand;

public class OpenCoverCommand implements ShellCommand {

    Command command ;
    Map<String,String> arguments = Maps.newHashMap();
    public OpenCoverCommand(String path) {
        command = Command.create(path + "/OpenCover.Console.Exe");
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
}
