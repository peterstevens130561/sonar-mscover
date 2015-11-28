package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.NoAssembliesDefinedException;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverTarget;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

public class OpenCoverCoverageRunnerBase implements TestRunner{
	private final static Logger LOG = LoggerFactory.getLogger(OpenCoverCoverageRunnerBase.class);
    private OpenCoverCommand openCoverCommand;
    private MsCoverConfiguration msCoverProperties;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private VsTestEnvironment testEnvironment;
    private CommandLineExecutor commandLineExecutor;
    private AssembliesFinder assembliesFinder;
    private VsTestRunnerCommandBuilder vsTestRunnerCommandBuilder;
    private VSTestStdOutParser vsTestStdOutParser;
	private Object c;
	private File coverageFile;
    public OpenCoverCoverageRunnerBase(OpenCoverCommand openCoverCommand,
            MsCoverConfiguration msCoverProperties, 
            VsTestEnvironment testEnvironment,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            CommandLineExecutor commandLineExecutor,
            AssembliesFinder assembliesFinder, 
            VsTestRunnerCommandBuilder vsTestRunnerCommandBuilder, 
            VSTestStdOutParser vsTestStdOutParser) {
        this.openCoverCommand = openCoverCommand;
        this.msCoverProperties = msCoverProperties;
        this.testEnvironment=testEnvironment;
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment;
        this.commandLineExecutor= commandLineExecutor;
        this.assembliesFinder=assembliesFinder;
        this.vsTestRunnerCommandBuilder = vsTestRunnerCommandBuilder;
        this.vsTestStdOutParser=vsTestStdOutParser;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.CoverageRunner#execute()
     */
    @Override
    public void execute() {
    	LOG.info("Invoked");
    	OpenCoverTarget openCoverTarget=vsTestRunnerCommandBuilder.build(false);
        VisualStudioSolution solution=microsoftWindowsEnvironment.getCurrentSolution();
        String targetDir=assembliesFinder.findUnitTestAssembliesDir(solution);
        openCoverCommand.setTargetCommand(openCoverTarget);
        openCoverCommand.setTargetDir(targetDir);
        
        List<String> excludeFilters = new ArrayList<String>();
        excludeFilters.add("*\\*.Designer.cs");
        openCoverCommand.setExcludeByFileFilter(excludeFilters);
        
        openCoverCommand.setExcludeFromCodeCoverageAttributeFilter();
        String filter = getAssembliesToIncludeInCoverageFilter(microsoftWindowsEnvironment.getAssemblies());
        openCoverCommand.setFilter(filter); 
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

        commandLineExecutor.execute(openCoverCommand);

        
    }
    public String getAssembliesToIncludeInCoverageFilter(List<String> assemblies) {
    	if(assemblies ==null || assemblies.size()==0) {
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
		this.coverageFile=coverageFile;
	}

}
