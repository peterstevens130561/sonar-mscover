package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverTarget;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

public class CommonOpenCoverTestRunnerBase implements OpenCoverTestRunner {
    private OpenCoverCommand openCoverCommand;
    private MsCoverConfiguration msCoverProperties;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private VsTestEnvironment testEnvironment;
    private AssembliesFinder assembliesFinder;
    private VsTestRunnerCommandBuilder vsTestRunnerCommandBuilder;

    private File coverageFile;
	private VSTestStdOutParser vsTestStdOutParser;
	private CommandLineExecutor commandLineExecutor;
	
    public CommonOpenCoverTestRunnerBase(MsCoverConfiguration msCoverProperties,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            OpenCoverCommand openCoverCommand,
            AssembliesFinder assembliesFinder, 
            VsTestRunnerCommandBuilder vsTestRunnerCommandBuilder,
            VSTestStdOutParser vsTestStdOutParser,
            CommandLineExecutor commandLineExecutor
            ) {
        this.openCoverCommand = openCoverCommand;
        this.msCoverProperties = msCoverProperties;
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment;
        this.assembliesFinder=assembliesFinder;
        this.vsTestRunnerCommandBuilder = vsTestRunnerCommandBuilder;
        this.vsTestStdOutParser = vsTestStdOutParser;
        this.commandLineExecutor = commandLineExecutor;
    }

	public CommonOpenCoverTestRunnerBase() {
		super();
	}

	protected void buildCommonArguments() {
		OpenCoverTarget openCoverTarget=vsTestRunnerCommandBuilder.build(false);
	    openCoverCommand.setTargetCommand(openCoverTarget);
	    
	    VisualStudioSolution solution=microsoftWindowsEnvironment.getCurrentSolution();
	    String targetDir=assembliesFinder.findUnitTestAssembliesDir(solution);
	    openCoverCommand.setTargetDir(targetDir);
	    
	    List<String> excludeFilters = new ArrayList<String>();
	    excludeFilters.add("*\\*.Designer.cs");
	    openCoverCommand.setExcludeByFileFilter(excludeFilters);
	    
	    openCoverCommand.setExcludeFromCodeCoverageAttributeFilter();
	    openCoverCommand.setRegister("user");
	    openCoverCommand.setMergeByHash();
	    
	
	    if(coverageFile==null) {
	    	openCoverCommand.setOutputPath(testEnvironment.getXmlCoveragePath());  
	    } else {
	    	openCoverCommand.setOutputPath(coverageFile.getAbsolutePath());
	    }
	    if(msCoverProperties.getOpenCoverSkipAutoProps()) {
	        openCoverCommand.setSkipAutoProps();
	    }
	}

	@Override
	public void setFilter(String filter) {
		openCoverCommand.setFilter(filter);
	}
	@Override
	public void execute() {
		buildCommonArguments();
		commandLineExecutor.execute(openCoverCommand);
	}

    @Override
    public File getTestResultsFile() {
    	vsTestStdOutParser.setStdOut(commandLineExecutor.getStdOut());
    	return vsTestStdOutParser.getTestResultsFile();
    }
	@Override
	public void setCoverageFile(File coverageFile) {
		this.coverageFile=coverageFile;
	}

}