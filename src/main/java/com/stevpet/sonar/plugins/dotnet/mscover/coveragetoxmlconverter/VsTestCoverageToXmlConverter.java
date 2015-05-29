package com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;

public class VsTestCoverageToXmlConverter implements CoverageToXmlConverter {

    private FileSystem fileSystem;
    private CodeCoverageCommand codeCoverageCommand;
    private CommandLineExecutor commandLineExecutor;

    @SuppressWarnings("ucd")
    public VsTestCoverageToXmlConverter(FileSystem fileSystem,
            CodeCoverageCommand codeCoverageCommand,
            CommandLineExecutor commandLineExecutor) {
        this.fileSystem = fileSystem;
        this.codeCoverageCommand = codeCoverageCommand;
        this.commandLineExecutor = commandLineExecutor;
    }

    @Override
    public void convert(File destination, File source) {
        File workDir = fileSystem.workDir();
        String sonarPath = workDir.getAbsolutePath();
        codeCoverageCommand.setSonarPath(sonarPath);
        codeCoverageCommand.setCoveragePath(source.getAbsolutePath());
        codeCoverageCommand.setOutputPath(destination.getAbsolutePath());
        codeCoverageCommand.install();
        commandLineExecutor.execute(codeCoverageCommand);
    }

    @Override
    public File convertIfNeeded(File source) {
        String destinationPath = source.getAbsolutePath().replace(".coverage",
                ".xml");
        File destination = new File(destinationPath);
        if (transformationNeeded(destination, source)) {
            convert(destination, source);
        }
        return destination;

    }

    @SuppressWarnings("ucd")
    protected boolean transformationNeeded(File destination, File source) {
        return !destination.exists()
                || FileUtils.isFileNewer(source, destination);

    }
}
