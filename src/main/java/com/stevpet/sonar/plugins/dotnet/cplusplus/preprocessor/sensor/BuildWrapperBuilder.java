package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.BatchExtension;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.command.Command;

import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ShellCommand;

public class BuildWrapperBuilder implements ShellCommand, BatchExtension {

    private String installDir;
    private String msBuildDir;
    private String outputPath;
    private String msBuildOptions;
    private Command command;
    public BuildWrapperBuilder setInstallDir(String installDir) {
        this.installDir=installDir;
        return this;
    }
    
    public BuildWrapperBuilder setMsBuildDir(String msBuildDir) {
        this.msBuildDir=msBuildDir;
        return this;
    }
    
    /**
     * Set the directory where wrapper should store its output
     * @param outputPath
     * @return this
     */
    public BuildWrapperBuilder setOutputPath(String outputPath) {
        this.outputPath=outputPath;
        return this;
    }
    
    @Override
    public String toCommandLine() {
        // TODO Auto-generated method stub
        return toCommand().toString();
    }

    public BuildWrapperBuilder setMsBuildOptions(String options) {
        this.msBuildOptions = options;
        return this;
    }
    @Override
    public Command toCommand() {
        File executable = new File(installDir,"build-wrapper.exe");
        String path=executable.getAbsolutePath();
        command = Command.create(path);
        command.addArgument("--out-dir");
        command.addArgument(CommandHelper.parenthesizeArgument(outputPath));
        String msBuildPath=CommandHelper.parenthesizeArgument(msBuildDir + "\\msbuild");
        command.addArgument(msBuildPath);
        command.addArgument("/t:Rebuild");
        if(StringUtils.isNotEmpty(msBuildOptions)) {
            command.addArgument(msBuildOptions);
        }
        return command;
    }

}
