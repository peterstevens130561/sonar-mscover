package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.picocontainer.DefaultPicoContainer;





import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

/**
 * Directs the workflowsteps for the usual test coverage steps
 *
 */
public class DefaultDirector implements WorkflowDirector {

	private final WorkflowSteps workflowSteps;
	private DefaultPicoContainer picoContainer;

	public DefaultDirector(WorkflowSteps workflowSteps) {
		this.workflowSteps = workflowSteps;
	}
	@Override
	public void wire(DefaultPicoContainer container) {
		this.picoContainer = container;
		workflowSteps.getComponents(picoContainer);
	    picoContainer.addComponent(workflowSteps.getCoverageParser())
	    .addComponent(workflowSteps.getCoverageSaver())
	    .addComponent(workflowSteps.getTestResultsParser())
	    .addComponent(workflowSteps.getTestResultsSaver())
	    .addComponent(workflowSteps.getTestRunner());
	}

	@Override
	public void execute() {
		VsTestEnvironment testEnvironment = picoContainer.getComponent(VsTestEnvironment.class);
		if(StringUtils.isEmpty(testEnvironment.getXmlCoveragePath())) {
			testEnvironment.setCoverageXmlPath("coverage.xml");
		}
		TestRunner runner = picoContainer.getComponent(TestRunner.class);
		runner.execute();
		
		File testResultsFile=runner.getTestResultsFile();
		testEnvironment.setTestResultsFile(testResultsFile);
		
		File coverageFile=new File(testEnvironment.getXmlCoveragePath());
        SonarCoverage sonarCoverage= new SonarCoverage();
        CoverageParserStep parser = picoContainer.getComponent(CoverageParserStep.class);
        parser.parse(sonarCoverage,coverageFile);
        
        CoverageSaver coverageSaver = picoContainer.getComponent(CoverageSaver.class);
        coverageSaver.save(sonarCoverage);
        
        TestResultsBuilder testResultsBuilder = picoContainer.getComponent(TestResultsBuilder.class);
        ProjectUnitTestResults testResults=testResultsBuilder.parse(testResultsFile, coverageFile);
        
        TestResultsSaver testResultsSaver = picoContainer.getComponent(TestResultsSaver.class);
        testResultsSaver.save(testResults);	
	}

}
