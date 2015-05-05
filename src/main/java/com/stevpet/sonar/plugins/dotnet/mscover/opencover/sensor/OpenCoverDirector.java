package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.TestResultsCleaner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowSteps;

public class OpenCoverDirector implements WorkflowDirector {

    private DefaultPicoContainer picoContainer;
	private WorkflowSteps workflowSteps;

    public OpenCoverDirector(WorkflowSteps workflowSteps) {
    	this.workflowSteps=workflowSteps;
    }
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.WorkflowDirector#wire(org.picocontainer.DefaultPicoContainer)
	 */
    @Override
	public void wire(DefaultPicoContainer container) {
        this.picoContainer=container;
        container
        .addComponent(TestResultsCleaner.class)
        .addComponent(InjectingFakesRemover.class);
    	addSteps();
    }
    
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.WorkflowDirector#execute()
	 */
    @Override
	public void execute() {
        TestResultsCleaner testResultsCleaner = picoContainer.getComponent(TestResultsCleaner.class);
        testResultsCleaner.execute();
        
        InjectingFakesRemover fakesRemover=picoContainer.getComponent(InjectingFakesRemover.class);
        fakesRemover.execute();
        
        VsTestEnvironment testEnvironment = picoContainer.getComponent(VsTestEnvironment.class);
       File file=executeVsTestOpenCoverRunner(); 
       testEnvironment.setTestResultsXmlPath(file.getAbsolutePath());
       SonarCoverage sonarCoverageRegistry=parseCoverageFile(testEnvironment);
       testEnvironment.setSonarCoverage(sonarCoverageRegistry);
       testEnvironment.setTestsHaveRun();
    }
    
    private void addSteps() {
    	picoContainer.addComponent(workflowSteps.getTestRunner())
    	.addComponent(workflowSteps.getCoverageSaver())
    	.addComponent(workflowSteps.getCoverageReader())
    	.addComponent(workflowSteps.getTestResultsBuilder())
    	.addComponent(workflowSteps.getTestResultsSaver());
    	workflowSteps.getComponents(picoContainer);
    }

    private File executeVsTestOpenCoverRunner() {
        TestRunner runner = picoContainer.getComponent(OpenCoverCoverageRunner.class);
        runner.execute();
        return runner.getTestResultsFile();
    }
    
    private SonarCoverage parseCoverageFile(VsTestEnvironment testEnvironment) {
        SonarCoverage sonarCoverageRegistry = new SonarCoverage();

        CoverageReader parser = picoContainer.getComponent(CoverageReader.class);
        parser.read(sonarCoverageRegistry,new File(testEnvironment.getXmlCoveragePath()));
        return sonarCoverageRegistry;
    }

}
