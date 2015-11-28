package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class OpenCoverCoverageSaver extends CoverageSaverBase implements
		BatchExtension {

	public OpenCoverCoverageSaver(
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
			PathResolver pathResolver, 
			FileSystem fileSystem) {
		super(
				new DefaultBranchFileCoverageSaver(
						new DefaultResourceResolver(pathResolver, fileSystem)), 
				new DefaultLineFileCoverageSaver(
						new DefaultResourceResolver(pathResolver, fileSystem)), 
				microsoftWindowsEnvironment);

	}

}
