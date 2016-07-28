package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.common.commandexecutor.CommandLineExecutorWithEvents;
import com.stevpet.sonar.plugins.common.commandexecutor.LineReceivedEvent;
import com.stevpet.sonar.plugins.common.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.common.commandexecutor.NullProcessLock;
import com.stevpet.sonar.plugins.common.commandexecutor.TimeoutException;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.NoAssembliesDefinedException;
import com.stevpet.sonar.plugins.dotnet.mscover.housekeeping.CommandAndChildrenRemover;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverTarget;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

public class DefaultOpenCoverTestRunner implements OpenCoverTestRunner {
    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverTestRunner.class);
	private static final int DEFAULT_TIMEOUT = 30;
    private static final int DEFAULT_RETRIES = 3;
    private OpenCoverCommand openCoverCommand;
	private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
	private AssembliesFinder assembliesFinder;
	private VsTestRunnerCommandBuilder vsTestRunnerCommandBuilder;

	private File coverageFile;
	private VSTestStdOutParser vsTestStdOutParser;
	private CommandLineExecutorWithEvents commandLineExecutor;
    private Pattern testProjectPattern;
    private int timeout = DEFAULT_TIMEOUT;
    private OpenCoverCommandLineConfiguration configuration;
    private StringBuilder sb;
    private long lastTrigger;
    private long lapse;
    private LocalDateTime previous;
    private int retries = DEFAULT_RETRIES ;
	public DefaultOpenCoverTestRunner(OpenCoverCommandLineConfiguration openCoverCommandLineConfiguration,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			OpenCoverCommand openCoverCommand,
			AssembliesFinder assembliesFinder,
			VsTestRunnerCommandBuilder vsTestRunnerCommandBuilder,
			VSTestStdOutParser vsTestStdOutParser,
			CommandLineExecutorWithEvents commandLineExecutor) {
		this.openCoverCommand = openCoverCommand;
		this.configuration = openCoverCommandLineConfiguration;
		this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
		this.assembliesFinder = assembliesFinder;
		this.vsTestRunnerCommandBuilder = vsTestRunnerCommandBuilder;
		this.vsTestStdOutParser = vsTestStdOutParser;
		this.commandLineExecutor = commandLineExecutor;
	}

	public static DefaultOpenCoverTestRunner create(
			MsCoverConfiguration msCoverProperties,Settings settings,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			FileSystem fileSystem) {
		return new DefaultOpenCoverTestRunner(new DefaultOpenCoverCommandLineConfiguration(settings),
				microsoftWindowsEnvironment, new OpenCoverCommand(), new DefaultAssembliesFinder(
						msCoverProperties), new VsTestRunnerCommandBuilder(
						msCoverProperties, microsoftWindowsEnvironment,
						fileSystem, new VsTestConfigFinder(),
						new VSTestCommand(), new DefaultAssembliesFinder(
								msCoverProperties)), new VSTestStdOutParser(),
				new LockedWindowsCommandLineExecutor(new NullProcessLock()));
	}

	protected void buildCommonArguments() {
	    Preconditions.checkNotNull(testProjectPattern, "TestProjectPattern not set");
	    Preconditions.checkNotNull(coverageFile,"CoverageFile not set");
	    vsTestRunnerCommandBuilder.setTestProjectPattern(testProjectPattern);
		OpenCoverTarget openCoverTarget = vsTestRunnerCommandBuilder
				.build(false);
	    openCoverCommand.setRegister(configuration.getRegister());
		openCoverCommand.setTargetCommand(openCoverTarget);
		openCoverCommand.setInstallDir(configuration.getInstallDir());
		VisualStudioSolution solution = microsoftWindowsEnvironment
				.getCurrentSolution();
		String targetDir = assembliesFinder.findUnitTestAssembliesDir(solution);
		openCoverCommand.setTargetDir(targetDir);

		List<String> excludeFilters = new ArrayList<String>();
		excludeFilters.add("*\\*.Designer.cs");
		openCoverCommand.setExcludeByFileFilter(excludeFilters);

		openCoverCommand.setExcludeFromCodeCoverageAttributeFilter();

		openCoverCommand.setMergeByHash();
		openCoverCommand.setOutputPath(coverageFile.getAbsolutePath());
		openCoverCommand.setHideSkipped("all");
		
		if (configuration.getSkipAutoProps()) {
			openCoverCommand.setSkipAutoProps();
		}
	}

    @Override
    public void execute() {

        buildCommonArguments();
        clearLog();
        commandLineExecutor.addLineReceivedListener(event -> logAppend(event));
        for (int retry = 0; retry <= retries; retry++) {
            try {
                commandLineExecutor.execute(openCoverCommand, timeout);
                return;
            } catch (TimeoutException t) {
                LOG.error("Timeout occurred on try {}",retry);
                logPrint();
                String commandLine = openCoverCommand.toCommandLine();
                CommandAndChildrenRemover remover = new CommandAndChildrenRemover();
                remover.cancel(commandLine);
            }
        }
        String msg="Failed after several tries on " + openCoverCommand.toCommandLine();
        LOG.error(msg);
        throw new IllegalStateException(msg);
        
    }

	private void clearLog() {
	    previous=LocalDateTime.now();
	    sb = new StringBuilder(2048);
    }

	private void logAppend(LineReceivedEvent event) {
	    LocalDateTime now  = event.getDateTime();
	    Duration duration=Duration.between(now, previous);
	    previous=now;
	    sb.append(String.format("%03d ",duration.getSeconds()));
	    sb.append(event.getLine());
	    sb.append("\n");
	}
	
	private void logPrint() {
	    LOG.info(sb.toString());
	    LOG.info("last trigger was {}s",(System.currentTimeMillis() - lastTrigger)/1000);
	}


    @Override
	public OpenCoverTestRunner onlyReportAssembliesOfTheSolution() {
		List<String> assemblies = microsoftWindowsEnvironment.getAssemblies();
		String filter = getAssembliesToIncludeInCoverageFilter(assemblies);
		openCoverCommand.setFilter(filter);
		return this;
	}
    
    @Override
    public OpenCoverTestRunner setRetries(@Nonnull int retries) {
        Preconditions.checkArgument(retries>-1,"retries should be >-1");
        this.retries=retries;
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

	@Override
	public void setTestCaseFilter(String testCaseFilter) {
		this.vsTestRunnerCommandBuilder.setTestCaseFilter(testCaseFilter);
		
	}

    @Override
    public void setTestProjectPattern(@Nonnull Pattern pattern) {
        this.testProjectPattern=pattern;
    }
    
    @Override
    public void setTimeout(int timeout) {
        this.timeout=timeout;
    }

}