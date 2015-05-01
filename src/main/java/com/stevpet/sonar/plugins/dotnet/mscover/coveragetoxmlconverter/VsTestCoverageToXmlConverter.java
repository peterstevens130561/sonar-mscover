package com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter;

import java.io.File;

import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;

public class VsTestCoverageToXmlConverter implements CoverageToXmlConverter {

	private FileSystem fileSystem;
	private CodeCoverageCommand codeCoverageCommand;
	private CommandLineExecutor commandLineExecutor;
	public VsTestCoverageToXmlConverter (FileSystem fileSystem,
			CodeCoverageCommand codeCoverageCommand,
			CommandLineExecutor commandLineExecutor) {
		this.fileSystem = fileSystem;
		this.codeCoverageCommand = codeCoverageCommand;
		this.commandLineExecutor = commandLineExecutor;
	}


	@Override
	public void convert(String destination, String source) {
		File workDir=fileSystem.workDir();
		String sonarPath = workDir.getAbsolutePath();
		codeCoverageCommand.setSonarPath(sonarPath);
		codeCoverageCommand.setCoveragePath(source);
		codeCoverageCommand.setOutputPath(destination);
		codeCoverageCommand.install();
		commandLineExecutor.execute(codeCoverageCommand);
	}
}

