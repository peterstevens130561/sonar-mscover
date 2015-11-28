package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.common.commandexecutor.DefaultProcessLock;
import com.stevpet.sonar.plugins.common.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
/**
 * Runs vstest embedded in OpenCover intended to be used directly in Sensor

 */
public class OpenCoverCoverageRunner extends OpenCoverCoverageRunnerBase
		implements BatchExtension {


	public OpenCoverCoverageRunner(
			MsCoverConfiguration msCoverProperties,
			VsTestEnvironment testEnvironment,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			FileSystem fileSystem) {
		super(
				new OpenCoverCommand(msCoverProperties), 
				msCoverProperties, 
				testEnvironment,
				microsoftWindowsEnvironment, 
				new LockedWindowsCommandLineExecutor(
						new DefaultProcessLock()
						), 
				new DefaultAssembliesFinder(msCoverProperties),
				new VsTestRunnerCommandBuilder(
						msCoverProperties, 
						microsoftWindowsEnvironment, 
						fileSystem, 
						new VsTestConfigFinder(), 
						new VSTestCommand(), 
						new DefaultAssembliesFinder(msCoverProperties)
						), 
						new VSTestStdOutParser()
				);
	}

}
