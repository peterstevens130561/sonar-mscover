package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.NullCoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.nullsaver.NullCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.NullTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.TestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.nullrestrunner.NullTestRunner;

public class NullWorkflowSteps implements WorkflowSteps {

    @Override
    public Class<? extends TestRunner> getTestRunner() {
        return NullTestRunner.class;
    }

    @Override
    public Class<? extends CoverageReader> getCoverageReader() {

        return NullCoverageReader.class;
    }

    @Override
    public Class<? extends TestResultsBuilder> getTestResultsBuilder() {
        return NullTestResultsBuilder.class;
    }

    @Override
    public Class<? extends CoverageSaver> getCoverageSaver() {
        return NullCoverageSaver.class;
    }

    @Override
    public Class<? extends TestResultsSaver> getTestResultsSaver() {
        return NullTestResultsSaver.class;
    }

    @Override
    public void getComponents(DefaultPicoContainer picoContainer) {

    }

}
