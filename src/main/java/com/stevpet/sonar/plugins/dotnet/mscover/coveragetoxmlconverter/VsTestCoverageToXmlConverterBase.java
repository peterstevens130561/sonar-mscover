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
package com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter;

import java.io.File;
import java.util.Collection;
import javax.annotation.Nonnull;

import org.apache.commons.io.FileUtils;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.common.commandexecutor.ProcessLock;

public class VsTestCoverageToXmlConverterBase implements BinaryCoverageToXmlConverter {

	private FileSystem fileSystem;
	private CodeCoverageCommand codeCoverageCommand;
	private CommandLineExecutor commandLineExecutor;
	private ProcessLock processLock;

	@SuppressWarnings("ucd")
	public VsTestCoverageToXmlConverterBase(FileSystem fileSystem,
			CodeCoverageCommand codeCoverageCommand,
			CommandLineExecutor commandLineExecutor, ProcessLock processLock) {
		this.fileSystem = fileSystem;
		this.codeCoverageCommand = codeCoverageCommand;
		this.commandLineExecutor = commandLineExecutor;
		this.processLock = processLock;
	}

	@Override
	public File convertFiles(@Nonnull File source) {
		File result;
		processLock.lock("convertCoverage");
		try {
			if (source.isDirectory()) {
				convertDirectory(source);
				result=source;
			} else {
				result=convertFile(source);
			}
		} finally {
			processLock.release();
		}
		return result;
	}

	private File convertFile(File source) {
		String destinationPath = source.getAbsolutePath().replace(".coverage",
				".xml");
		File destination = new File(destinationPath);
		if (!destination.exists()) {
			File workDir = fileSystem.workDir();
			String sonarPath = workDir.getAbsolutePath();
			codeCoverageCommand.setSonarPath(sonarPath);
			codeCoverageCommand.setCoveragePath(source.getAbsolutePath());
			codeCoverageCommand.setOutputPath(destination.getAbsolutePath());
			codeCoverageCommand.install();
			commandLineExecutor.execute(codeCoverageCommand);
			FileUtils.deleteQuietly(source);
		}
		return destination;
	}

	private void convertDirectory(File integrationTestsDir) {
		Collection<File> files = FileUtils.listFiles(integrationTestsDir,
				new String[] { "coverage" }, true);
		for (File coverageFile : files) {
			convertFile(coverageFile);
		}
	}

	@Override
	public void convert(File file, File file2) {
		// TODO Auto-generated method stub
		
	}
}
