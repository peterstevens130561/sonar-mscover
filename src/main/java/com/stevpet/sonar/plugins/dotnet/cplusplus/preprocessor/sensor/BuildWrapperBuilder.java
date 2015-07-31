package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import java.io.File;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.utils.command.Command;

import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ShellCommand;

public class BuildWrapperBuilder implements ShellCommand, BatchExtension {
    private static final String EXECUTABLE = "build-wrapper.exe";
    private static Logger LOG = LoggerFactory.getLogger(BuildWrapperBuilder.class);
    private String installDir;
    private String outputPath;
    private String msBuildOptions;
    private Command command;

    public BuildWrapperBuilder setInstallDir(String installDir) {
        this.installDir = installDir;
        return this;
    }

    /**
     * Set the directory where wrapper should store its output
     * 
     * @param outputPath
     * @return this
     */
    public BuildWrapperBuilder setOutputPath(String outputPath) {
        this.outputPath = outputPath;
        return this;
    }


    @Override
    public String toCommandLine() {
        return toCommand().toString();
    }

    public BuildWrapperBuilder setMsBuildOptions(String options) {
        this.msBuildOptions = options;
        return this;
    }

    @Override
    public Command toCommand() {
        String envPath=System.getenv("PATH");
        LOG.info("----> PATH is set to " + envPath);
        File executable = new File(installDir, EXECUTABLE);
        if (!executable.exists()) {
            String msg = "Executable does not exist: " + executable.getAbsolutePath();
            LOG.error(msg);

            while (executable != null && !executable.exists()) {
                LOG.error("-- trying " + executable.getAbsolutePath());
                executable = executable.getParentFile();
            }
            if (executable != null) {
                LOG.error("-- path found at " + executable.getAbsolutePath());
            }

            throw new BuildWrapperException(msg);
        }
        String path = executable.getAbsolutePath();
        command = Command.create(path);
        command.addArgument("--out-dir");
        command.addArgument(CommandHelper.parenthesizeArgument(outputPath));
        String msBuildPath = "msbuild";
        command.addArgument(msBuildPath);
        command.addArgument("/t:Rebuild");
        if (StringUtils.isNotEmpty(msBuildOptions)) {
            command.addArgument(msBuildOptions);
        } 
        return command;
    }


    @Override
    public String getExecutable() {
        return EXECUTABLE;
    }

}
