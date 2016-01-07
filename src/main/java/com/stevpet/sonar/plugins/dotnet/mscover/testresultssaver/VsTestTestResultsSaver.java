package com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.DefaultTestResultsFormatter;

public class VsTestTestResultsSaver extends VsTestTestResultsSaverBase
		implements BatchExtension {

	public VsTestTestResultsSaver(
			PathResolver pathResolver, FileSystem filesystem) {
		super(
			new DefaultResourceResolver(pathResolver,filesystem), 
			new DefaultTestResultsFormatter()
		);
	}

}
