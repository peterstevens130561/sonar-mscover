package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.common.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.NoAssembliesDefinedException;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class OpenCoverUnitTestCoverageRunnerBase implements TestRunner{
	private final static Logger LOG = LoggerFactory.getLogger(OpenCoverUnitTestCoverageRunnerBase.class);
    MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
	private OpenCoverTestRunner openCoverTestRunner;
	
    public OpenCoverUnitTestCoverageRunnerBase(
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            OpenCoverTestRunner openCoverTestRunner) {

        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment;
        this.openCoverTestRunner=openCoverTestRunner;
        
    }

	public OpenCoverUnitTestCoverageRunnerBase(
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment2,
			CommonOpenCoverTestRunner commonOpenCoverTestRunner) {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.CoverageRunner#execute()
     */
    @Override
    public void execute() {
    	LOG.info("Invoked");
    	List<String> assemblies=microsoftWindowsEnvironment.getAssemblies();
    	String filter=getAssembliesToIncludeInCoverageFilter(assemblies);
    	openCoverTestRunner.setFilter(filter);
    	openCoverTestRunner.execute();
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
    	return openCoverTestRunner.getTestResultsFile();
    }
	@Override
	public void setCoverageFile(File coverageFile) {
		openCoverTestRunner.setCoverageFile(coverageFile);
	}

}
