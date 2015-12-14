package com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.common.commandexecutor.DefaultProcessLock;
import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;

public class VsTestCoverageToXmlConverter extends
		VsTestCoverageToXmlConverterBase implements BatchExtension {

	public VsTestCoverageToXmlConverter(FileSystem fileSystem) {
		super(fileSystem, new WindowsCodeCoverageCommand(), new WindowsCommandLineExecutor(), new DefaultProcessLock());
		// TODO Auto-generated constructor stub
	}

}
