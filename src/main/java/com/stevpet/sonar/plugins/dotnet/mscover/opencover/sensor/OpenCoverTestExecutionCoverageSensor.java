/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;
import java.util.Set;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.injectors.ConstructorInjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.DotNetConstants;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.AbstractDotNetSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.ProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.ConcreteOpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.CoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AbstractVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;
@DependsUpon(DotNetConstants.CORE_PLUGIN_EXECUTED)
@DependedUpon("OpenCoverRunningVsTest")
public class OpenCoverTestExecutionCoverageSensor extends AbstractDotNetSensor {

    private static String WONT_EXECUTE = "VsTest.Console using OpenCover.Console.Exe won't execute as ";
    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverTestExecutionCoverageSensor.class);
    private VisualStudioSolution solution;
    private final MsCoverProperties propertiesHelper ;
    private VsTestEnvironment testEnvironment;
    private CommandLineExecutor commandLineExecutor = new WindowsCommandLineExecutor();
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private OpenCoverCommand openCoverCommand = new OpenCoverCommand();
    private AssembliesFinderFactory assembliesFinderFactory = new AssembliesFinderFactory();
    private VSTestStdOutParser vsTestStdOutParser = new VSTestStdOutParser();
    private OpenCoverParserFactory openCoverParserFactory = new ConcreteOpenCoverParserFactory();
    private FakesRemover fakesRemover = new DefaultFakesRemover();
    private FileSystem fileSystem;
    private DefaultPicoContainer openCoverContainer;
    
    public OpenCoverTestExecutionCoverageSensor(MsCoverProperties propertiesHelper, 
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            FileSystem fileSystem,
            VsTestEnvironment testEnvironment) {
        super(microsoftWindowsEnvironment, propertiesHelper.getMode());
        this.propertiesHelper = propertiesHelper;
        this.fileSystem = fileSystem;
        this.testEnvironment = testEnvironment;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }

    @Override
    public String[] getSupportedLanguages() {
        return new String[] {"cs"};
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldExecuteOnProject(Project project) {
        if (testEnvironment.getTestsHaveRun()) {
            logReasonToNotExecute("test execution has already been done.");
            return false;
        }
        if (getMicrosoftWindowsEnvironment().getCurrentSolution() != null
                && getMicrosoftWindowsEnvironment().getCurrentSolution()
                        .getUnitTestProjects().isEmpty()) {
            logReasonToNotExecute("there are no test projects.");
            return false;
        }
        Set<String> languages = fileSystem.languages();
        if ( !languages.contains("cs")) {
            return false;
        }
        if (propertiesHelper.runOpenCover()) {
            LOG.info("will run opencover with vstest embedded");
            return true;
        }
        return false;
    }
    private void logReasonToNotExecute(String string) {
        LOG.info(WONT_EXECUTE + string);
    }

    @Override
    public void analyse(Project project, SensorContext context) {
        testEnvironment.setCoverageXmlFile(project,"coverage-report.xml");

        openCoverContainer = new DefaultPicoContainer(new ConstructorInjection());
        openCoverContainer.addComponent(new ProcessLock("opencover"))
        .addComponent(LockedWindowsCommandLineExecutor.class)
        .addComponent(propertiesHelper)
        .addComponent(testEnvironment)
        .addComponent(openCoverCommand)
        .addComponent(microsoftWindowsEnvironment)
        .addComponent(fileSystem)
        .addComponent(WindowsVsTestRunner.class)
        .addComponent(OpenCoverCoverageRunner.class);
        
        getSolution();
        AssembliesFinder finder = assembliesFinderFactory.create(propertiesHelper);
        String targetDir=finder.findUnitTestAssembliesDir(solution);
        fakesRemover.removeFakes(new File(targetDir)); 
       
        openCoverCommand.setTargetDir(targetDir);
        
        VsTestRunner unitTestRunner = openCoverContainer.getComponent(WindowsVsTestRunner.class);
        unitTestRunner.clean();
        VSTestCommand testCommand=unitTestRunner.prepareTestCommand();
        
        executeVsTestOpenCoverRunner(testCommand);
        getResultPaths();
        // tell that tests were executed so that no other project tries to launch them a second time
        testEnvironment.setTestsHaveRun();
        parseCoverageFile(testEnvironment);
    }

    private void parseCoverageFile(VsTestEnvironment testEnvironment) {
        SonarCoverage sonarCoverageRegistry = new SonarCoverage();

        XmlParserSubject parser=openCoverParserFactory.createOpenCoverParser(sonarCoverageRegistry,propertiesHelper);
        parser.parseFile(new File(testEnvironment.getXmlCoveragePath()));
        testEnvironment.setSonarCoverage(sonarCoverageRegistry);
    }

    
    
    private void executeVsTestOpenCoverRunner(VSTestCommand testCommand) {

        openCoverCommand.setTargetCommand(testCommand);
       
        CoverageRunner runner = openCoverContainer.getComponent(OpenCoverCoverageRunner.class);
        runner.execute();
    }
    
    /**
     * parse test log to get paths to result files
     */
    public void getResultPaths() {

        String stdOut=commandLineExecutor.getStdOut();
        vsTestStdOutParser.setResults(stdOut);
        String resultsPath=vsTestStdOutParser.getTestResultsXmlPath(); 
        testEnvironment.setTestResultsXmlPath(resultsPath);
    }

    private void getSolution() {
        solution = getMicrosoftWindowsEnvironment().getCurrentSolution();
        if (solution == null) {
            throw new SonarException("No .NET solution or project has been given to the Gallio command builder.");
        }
    }


    public void setOpenCoverCommand(OpenCoverCommand openCoverCommand) {
       this.openCoverCommand = openCoverCommand;
    }


    public void setOpenCoverCommandBuilder(OpenCoverCommandBuilder mock) {
        
    }

    public void setAssembliesFinderFactory(AssembliesFinderFactory assembliesFinderFactory) {
        this.assembliesFinderFactory=assembliesFinderFactory;
    }
 
    public void setCommandLineExecutor(CommandLineExecutor commandLineExecutor) {
        this.commandLineExecutor = commandLineExecutor;
    }

    /**
     * @param vsTestStdOutParser the vsTestStdOutParser to set
     */
    public void setVsTestStdOutParser(VSTestStdOutParser vsTestStdOutParser) {
        this.vsTestStdOutParser = vsTestStdOutParser;
    }

    /**
     * @param openCoverParserFactory the openCoverParserFactory to set
     */
    public void setOpenCoverParserFactory(
            OpenCoverParserFactory openCoverParserFactory) {
        this.openCoverParserFactory = openCoverParserFactory;
    }

    /**
     * @param fakesRemover the fakesRemover to set
     */
    public void setFakesRemover(FakesRemover fakesRemover) {
        this.fakesRemover = fakesRemover;
    }


}
