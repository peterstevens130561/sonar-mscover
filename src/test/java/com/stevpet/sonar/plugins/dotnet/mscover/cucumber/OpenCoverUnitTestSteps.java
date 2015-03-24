package com.stevpet.sonar.plugins.dotnet.mscover.cucumber;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.SensorContextMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.VisualStudioSolutionMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.OpenCoverTestExecutionCoverageSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.ModuleFileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class OpenCoverUnitTestSteps {
    
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private ModuleFileSystemMock moduleFileSystemMock = new ModuleFileSystemMock();
    private VsTestEnvironment vsTestEnvironment = new VsTestEnvironment();
    private OpenCoverTestExecutionCoverageSensor sensor ;
   
    @Given("^invoking OpenCoverTestExecutionCoverageSensor$")
    public void invoking_OpenCoverTestExecutionCoverageSensor() throws Throwable {
        sensor=new OpenCoverTestExecutionCoverageSensor(msCoverPropertiesMock.getMock(), 
                microsoftWindowsEnvironmentMock.getMock(), 
                moduleFileSystemMock.getMock(), 
                vsTestEnvironment);
    }

    @Given("^no unit tests in$")
    public void no_unit_tests_in() throws Throwable {
        VisualStudioSolutionMock solutionMock = new VisualStudioSolutionMock();
        microsoftWindowsEnvironmentMock.givenHasSolution(solutionMock.getMock());
        Project project = new Project("bogus");
        SensorContextMock contextMock = new SensorContextMock();
        sensor.analyse(project, contextMock.getMock());

    }

    @Then("^step represents the outcome of the event$")
    public void step_represents_the_outcome_of_the_event() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
    
    private void appendPluginProperty(String name, String value) {
    }
}
