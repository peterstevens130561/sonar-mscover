package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;

public class VsTestEmbeddedInOpenCoverCommandBuilder {
    private static Logger LOG = LoggerFactory.getLogger(DefaultVsTestRunnerFactory.class);

    private OpenCoverCommand openCoverCommand;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private VsTestRunner unitTestRunner;
    private MsCoverProperties propertiesHelper;
    private VsTestEnvironment testEnvironment;
    private VSTestStdOutParser vsTestStdOutParser;
       

    public VsTestEmbeddedInOpenCoverCommandBuilder(OpenCoverCommand openCoverCommand,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            VsTestRunner unitTestRunner, 
            MsCoverProperties propertiesHelper, 
            VsTestEnvironment testEnvironment) {
        this.openCoverCommand = openCoverCommand;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.unitTestRunner = unitTestRunner;
        this.propertiesHelper = propertiesHelper;
        this.testEnvironment=testEnvironment;           
    }
    
    public void execute() {
        unitTestRunner.clean();
        runVsTestInOpenCover();
        getResultPaths();
        testEnvironment.setTestsHaveRun();

    }
    
        public void runVsTestInOpenCover() {
            VSTestCommand testCommand=unitTestRunner.prepareTestCommand();
            openCoverCommand.setTargetCommand(testCommand);
            
            String path=propertiesHelper.getOpenCoverInstallPath();
            openCoverCommand.setCommandPath(path);
            
            List<String> excludeFilters = new ArrayList<String>();
            excludeFilters.add("*\\*.Designer.cs");
            openCoverCommand.setExcludeByFileFilter(excludeFilters);
            
            openCoverCommand.setExcludeFromCodeCoverageAttributeFilter();
            String filter = getAssembliesToIncludeInCoverageFilter();
            openCoverCommand.setFilter(filter); 
            openCoverCommand.setRegister("user");
            openCoverCommand.setMergeByHash();
            openCoverCommand.setOutputPath(testEnvironment.getXmlCoveragePath());  
            if(propertiesHelper.getOpenCoverSkipAutoProps()) {
                openCoverCommand.setSkipAutoProps();
            }

            openCoverCommand.setTargetDir(testEnvironment.getTargetDir());
            openCoverCommand.execute();

            
        }

    public String getAssembliesToIncludeInCoverageFilter() {
            final StringBuilder filterBuilder = new StringBuilder();
            // We add all the covered assemblies
            for (String assemblyName : microsoftWindowsEnvironment.getAssemblies()) {
              filterBuilder.append("+[" + assemblyName + "]* ");
            }
            return filterBuilder.toString();
        }

    
    /**
     * parse test log to get paths to result files
     */
    public void getResultPaths() {
        String stdOut=openCoverCommand.getStdOut();      
        vsTestStdOutParser.setResults(stdOut);
        String resultsPath=vsTestStdOutParser.getTestResultsXmlPath(); 
        testEnvironment.setTestResultsXmlPath(resultsPath);
    }
    

}
