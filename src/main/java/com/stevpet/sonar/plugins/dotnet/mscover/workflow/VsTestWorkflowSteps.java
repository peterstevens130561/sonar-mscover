package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.common.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReaderBase;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultBranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverBase;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultLineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.VsTestCoverageToXmlConverterBase;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.DefaultTestResultsFormatter;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.DefaultTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.DefaultTestResultsParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.TestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.WindowsVsTestRunnerBase;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder.VsTestFileNamesParser;

public class VsTestWorkflowSteps implements WorkflowSteps {

    @Override
    public Class<? extends TestRunner> getTestRunner() {
        return WindowsVsTestRunnerBase.class;
    }

    @Override
    public Class<? extends CoverageReader> getCoverageReader() {
        return CoverageReaderBase.class;
    }

    @Override
    public Class<? extends TestResultsBuilder> getTestResultsBuilder() {
        return DefaultTestResultsBuilder.class;
    }

    @Override
    public Class<? extends CoverageSaver> getCoverageSaver() {
        return CoverageSaverBase.class;
    }

    @Override
    public Class<? extends TestResultsSaver> getTestResultsSaver() {
        return DefaultTestResultsSaver.class;
    }

    public void getComponents(DefaultPicoContainer container) {
        
        getCoverageReaderComponents(container);
        getTestRunnerComponents(container);
        getTestResultsSaverComponents(container);
    }

    private void getCoverageReaderComponents(DefaultPicoContainer container) {
        container.addComponent(VsTestCoverageParser.class);
    }

    private void getTestResultsSaverComponents(DefaultPicoContainer container) {
        container.addComponent(
                DefaultTestResultsFormatter.class);
    }

    private void getTestRunnerComponents(DefaultPicoContainer container) {
        container
                .addComponent(OpenCoverCommand.class)
                .addComponent(LockedWindowsCommandLineExecutor.class)
                .addComponent(VsTestConfigFinder.class)
                .addComponent(WindowsCodeCoverageCommand.class)
                .addComponent(VSTestStdOutParser.class)
                .addComponent(DefaultAssembliesFinder.class)
                .addComponent(VsTestRunnerCommandBuilder.class)
                .addComponent(VSTestCommand.class)
                .addComponent(DefaultLineFileCoverageSaver.class)
                .addComponent(DefaultBranchFileCoverageSaver.class)
                .addComponent(VsTestCoverageToXmlConverterBase.class);
        addTestResultsBuilderComponents(container);
    }

    private void addTestResultsBuilderComponents(DefaultPicoContainer container) {
        container.addComponent(VsTestFileNamesParser.class).addComponent(
                DefaultTestResultsParser.class);
    }
}
