package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.TestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestBatchData;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowSteps;

/**
 * Directs the workflowsteps for the usual test coverage steps
 * 
 */
public class IntegrationTestWorkflowDirector implements  BatchExtension {

    private WorkflowSteps workflowSteps;
    private DefaultPicoContainer picoContainer;
 

    public IntegrationTestWorkflowDirector() {
        
    }

    public void wire(DefaultPicoContainer container) {
        this.picoContainer = container;
        workflowSteps = picoContainer.getComponent(WorkflowSteps.class);
        workflowSteps.getComponents(picoContainer);
        picoContainer.addComponent(workflowSteps.getCoverageReader()).addComponent(workflowSteps.getCoverageSaver())
                .addComponent(workflowSteps.getTestResultsBuilder()).addComponent(workflowSteps.getTestResultsSaver())
                .addComponent(workflowSteps.getTestRunner());
    }

    public void execute() {
        workflowSteps = picoContainer.getComponent(WorkflowSteps.class);

        TestRunner runner = picoContainer.getComponent(TestRunner.class);
        runner.execute();

        SonarCoverage sonarCoverage = new SonarCoverage();
        CoverageReader reader = picoContainer.getComponent(CoverageReader.class);
        reader.read(sonarCoverage, null);

        CoverageSaver coverageSaver = picoContainer.getComponent(CoverageSaver.class);
        coverageSaver.save(sonarCoverage);

    }

}
