package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;

/**
 * Build the openCoverCommand through direct communication with the objects that know
 * 
 * @author stevpet
 *
 */
public class OpenCoverCommandBuilder {

    private OpenCoverCommand openCoverCommand;
    private VisualStudioSolution solution;
    private MsCoverProperties msCoverProperties;
    private VsTestRunner unitTestRunner;
    private VsTestEnvironment testEnvironment;
    private List<String> assemblies;
    
    public OpenCoverCommandBuilder() {

    }
    
    public OpenCoverCommandBuilder setOpenCoverCommand(OpenCoverCommand openCoverCommand) {
        this.openCoverCommand = openCoverCommand;
        return this;
    }
    
 
    public OpenCoverCommandBuilder setMsCoverProperties(MsCoverProperties msCoverProperties) {
        this.msCoverProperties=msCoverProperties;
        return this;
    }
    
    public OpenCoverCommandBuilder setTestRunner(VsTestRunner unitTestRunner) {
        this.unitTestRunner = unitTestRunner;
        return this;
    }
    
    public OpenCoverCommandBuilder setTestEnvironment(
            VsTestEnvironment testEnvironment) {
        this.testEnvironment = testEnvironment;
        return this;
    }
    
    public void build() {
        VSTestCommand testCommand=unitTestRunner.prepareTestCommand();
        openCoverCommand.setTargetCommand(testCommand);
        
        String path=msCoverProperties.getOpenCoverInstallPath();
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
        if(msCoverProperties.getOpenCoverSkipAutoProps()) {
            openCoverCommand.setSkipAutoProps();
        }
    }
    

    public void setAssemblies(List<String> assemblies) {
        this.assemblies = assemblies;
    }
    public String getAssembliesToIncludeInCoverageFilter() {
        final StringBuilder filterBuilder = new StringBuilder();
        // We add all the covered assemblies
        for (String assemblyName : listCoveredAssemblies()) {
          filterBuilder.append("+[" + assemblyName + "]* ");
        }
        return filterBuilder.toString();
    }
    
    public List<String> listCoveredAssemblies() {
        return assemblies;
      }

}

