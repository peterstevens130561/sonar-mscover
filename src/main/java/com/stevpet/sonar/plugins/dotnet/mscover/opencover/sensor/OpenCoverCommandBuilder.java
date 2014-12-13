package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.util.ArrayList;
import java.util.List;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.VisualStudioSolutionMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AbstractAssembliesFinderFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;

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
    
    public OpenCoverCommandBuilder() {

    }
    
    public OpenCoverCommandBuilder setOpenCoverCommand(OpenCoverCommand openCoverCommand) {
        this.openCoverCommand = openCoverCommand;
        return this;
    }
    
    public OpenCoverCommandBuilder setSolution(VisualStudioSolution solution) {
        this.solution = solution;
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
    

    public String getAssembliesToIncludeInCoverageFilter() {
        final StringBuilder filterBuilder = new StringBuilder();
        // We add all the covered assemblies
        for (String assemblyName : listCoveredAssemblies()) {
          filterBuilder.append("+[" + assemblyName + "]* ");
        }
        return filterBuilder.toString();
    }
    
    public List<String> listCoveredAssemblies() {
        List<String> coveredAssemblyNames = new ArrayList<String>();
        for (VisualStudioProject visualProject : solution.getProjects()) {
            coveredAssemblyNames.add(visualProject.getAssemblyName());
        }
        return coveredAssemblyNames;
      }

    public void setOpenCoverCommand(OpenCoverCommandMock openCoverCommandMock) {
        setOpenCoverCommand(openCoverCommandMock.getMock());
    }

    public void setSolution(VisualStudioSolutionMock visualStudioSolutionMock) {
        setSolution(visualStudioSolutionMock.getMock());
    }

    public void setTestRunner(VsTestRunnerMock vsTestRunnerMock) {
        setTestRunner(vsTestRunnerMock.getMock());
    }

    public void setMsCoverProperties(MsCoverPropertiesMock msCoverPropertiesMock) {
        setMsCoverProperties(msCoverPropertiesMock.getMock());
    }

    public void setTestEnvironment(VsTestEnvironmentMock vsTestEnvironmentMock) {
        setTestEnvironment(vsTestEnvironmentMock.getMock());
    }








}