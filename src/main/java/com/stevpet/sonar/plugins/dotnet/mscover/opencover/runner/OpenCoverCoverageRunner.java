package com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.NoAssembliesDefinedException;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;

public class OpenCoverCoverageRunner implements CoverageRunner {
    private OpenCoverCommand openCoverCommand;
    private MsCoverProperties msCoverProperties;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private VsTestEnvironment testEnvironment;
    private CommandLineExecutor commandLineExecutor;

    public OpenCoverCoverageRunner(OpenCoverCommand openCoverCommand,
            MsCoverProperties msCoverProperties, 
            VsTestEnvironment testEnvironment,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            CommandLineExecutor commandLineExecutor) {
        this.openCoverCommand = openCoverCommand;
        this.msCoverProperties = msCoverProperties;
        this.testEnvironment=testEnvironment;
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment;
        this.commandLineExecutor= commandLineExecutor;
        
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.CoverageRunner#execute()
     */
    @Override
    public void execute() {
    
        String path=msCoverProperties.getOpenCoverInstallPath();
        openCoverCommand.setCommandPath(path); // is configurable, so let's not inject
        
        List<String> excludeFilters = new ArrayList<String>();
        excludeFilters.add("*\\*.Designer.cs");
        openCoverCommand.setExcludeByFileFilter(excludeFilters);
        
        openCoverCommand.setExcludeFromCodeCoverageAttributeFilter();
        String filter = getAssembliesToIncludeInCoverageFilter(microsoftWindowsEnvironment.getAssemblies());
        openCoverCommand.setFilter(filter); 
        openCoverCommand.setRegister("user");
        openCoverCommand.setMergeByHash();
        openCoverCommand.setOutputPath(testEnvironment.getXmlCoveragePath());  
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
 
        /* (non-Javadoc)
         * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.CoverageRunner#getStdOut()
         */
        @Override
        public String getStdOut() {
            return commandLineExecutor.getStdOut();
        }
}
