package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.common.commandexecutor.DefaultProcessLock;
import com.stevpet.sonar.plugins.common.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.NoAssembliesDefinedException;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverTarget;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

public class DefaultOpenCoverTestRunner implements OpenCoverTestRunner {
	private OpenCoverCommand openCoverCommand;
	private MsCoverConfiguration msCoverProperties;
	private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
	private VsTestEnvironment testEnvironment;
	private AssembliesFinder assembliesFinder;
	private VsTestRunnerCommandBuilder vsTestRunnerCommandBuilder;

	private File coverageFile;
	private VSTestStdOutParser vsTestStdOutParser;
	private CommandLineExecutor commandLineExecutor;

	public DefaultOpenCoverTestRunner(MsCoverConfiguration msCoverProperties,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			OpenCoverCommand openCoverCommand,
			AssembliesFinder assembliesFinder,
			VsTestRunnerCommandBuilder vsTestRunnerCommandBuilder,
			VSTestStdOutParser vsTestStdOutParser,
			CommandLineExecutor commandLineExecutor,
			VsTestEnvironment testEnvironment) {
		this.openCoverCommand = openCoverCommand;
		this.msCoverProperties = msCoverProperties;
		this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
		this.assembliesFinder = assembliesFinder;
		this.vsTestRunnerCommandBuilder = vsTestRunnerCommandBuilder;
		this.vsTestStdOutParser = vsTestStdOutParser;
		this.commandLineExecutor = commandLineExecutor;
		this.testEnvironment=testEnvironment;
	}

	public static DefaultOpenCoverTestRunner create(
			MsCoverConfiguration msCoverProperties,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			FileSystem fileSystem,
			VsTestEnvironment vsTestEnvironment) {
		return new DefaultOpenCoverTestRunner(msCoverProperties,
				microsoftWindowsEnvironment, new OpenCoverCommand(
						msCoverProperties), new DefaultAssembliesFinder(
						msCoverProperties), new VsTestRunnerCommandBuilder(
						msCoverProperties, microsoftWindowsEnvironment,
						fileSystem, new VsTestConfigFinder(),
						new VSTestCommand(), new DefaultAssembliesFinder(
								msCoverProperties)), new VSTestStdOutParser(),
				new LockedWindowsCommandLineExecutor(new DefaultProcessLock()),
				vsTestEnvironment);
	}

	protected void buildCommonArguments() {
		OpenCoverTarget openCoverTarget = vsTestRunnerCommandBuilder
				.build(false);
		openCoverCommand.setTargetCommand(openCoverTarget);

		VisualStudioSolution solution = microsoftWindowsEnvironment
				.getCurrentSolution();
		String targetDir = assembliesFinder.findUnitTestAssembliesDir(solution);
		openCoverCommand.setTargetDir(targetDir);

		List<String> excludeFilters = new ArrayList<String>();
		excludeFilters.add("*\\*.Designer.cs");
		openCoverCommand.setExcludeByFileFilter(excludeFilters);

		openCoverCommand.setExcludeFromCodeCoverageAttributeFilter();
		openCoverCommand.setRegister("user");
		openCoverCommand.setMergeByHash();

		if (coverageFile == null) {
			openCoverCommand
					.setOutputPath(testEnvironment.getXmlCoveragePath());
		} else {
			openCoverCommand.setOutputPath(coverageFile.getAbsolutePath());
		}
		if (msCoverProperties.getOpenCoverSkipAutoProps()) {
			openCoverCommand.setSkipAutoProps();
		}
	}

	@Override
	public void execute() {
		buildCommonArguments();
		commandLineExecutor.execute(openCoverCommand);
	}

	@Override
	public OpenCoverTestRunner onlyReportAssembliesOfTheSolution() {
		List<String> assemblies = microsoftWindowsEnvironment.getAssemblies();
		String filter = getAssembliesToIncludeInCoverageFilter(assemblies);
		openCoverCommand.setFilter(filter);
		return this;
	}

	public String getAssembliesToIncludeInCoverageFilter(List<String> assemblies) {
		if (assemblies == null || assemblies.size() == 0) {
			throw new NoAssembliesDefinedException();
		}
		final StringBuilder filterBuilder = new StringBuilder();
		// We add all the covered assemblies
		for (String assemblyName : assemblies) {
			filterBuilder.append("+[" + assemblyName + "]* ");
		}
		return filterBuilder.toString();
	}

	@Override
	public File getTestResultsFile() {
		vsTestStdOutParser.setStdOut(commandLineExecutor.getStdOut());
		return vsTestStdOutParser.getTestResultsFile();
	}

	@Override
	public void setCoverageFile(File coverageFile) {
		this.coverageFile = coverageFile;
	}

}