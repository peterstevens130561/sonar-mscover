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
import org.picocontainer.injectors.AnnotatedFieldInjection;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.injectors.MultiInjection;
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
import com.stevpet.sonar.plugins.dotnet.mscover.Wirer;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.ProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.ConcreteOpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.SonarBranchSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.SonarCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.SonarLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AbstractVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.BaseAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;
@DependsUpon(DotNetConstants.CORE_PLUGIN_EXECUTED)
@DependedUpon("OpenCoverRunningVsTest")
public class OpenCoverTestExecutionCoverageSensor extends AbstractDotNetSensor {

    private static String WONT_EXECUTE = "VsTest.Console using OpenCover.Console.Exe won't execute as ";
    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverTestExecutionCoverageSensor.class);
    private static final Object OpenCoverSourceFileNamesObserver = null;
    private VisualStudioSolution solution;
    private final MsCoverProperties propertiesHelper ;
    private VsTestEnvironment testEnvironment;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

    private FileSystem fileSystem;
    
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
        testEnvironment.setSonarCoverage(new SonarCoverage());
        AssembliesFinder assembliesFinder = new BaseAssembliesFinder(propertiesHelper, microsoftWindowsEnvironment);
        String targetDir=assembliesFinder.findUnitTestAssembliesDir(microsoftWindowsEnvironment.getCurrentSolution());
        testEnvironment.setTargetDir(targetDir);
        getSolution();
 
        OpenCoverCoverageParser openCoverCoverageParser = Wirer.instantiateOpenCoverCoverageParser(testEnvironment, propertiesHelper);
       
        DefaultPicoContainer picoContainer = new DefaultPicoContainer(new ConstructorInjection());
        picoContainer.addComponent(VsTestEmbeddedInOpenCoverCommandBuilder.class)
            .addComponent(OpenCoverCommand.class)
            .addComponent(OpenCoverCommandBuilder.class)
            .addComponent(WindowsVsTestRunner.class)
            .addComponent(WindowsCommandLineExecutor.class)
            .addComponent(propertiesHelper)
            .addComponent(testEnvironment)
            .addComponent(fileSystem)
            .addComponent(DefaultFakesRemover.class)
            .addComponent(VSTestStdOutParser.class)
            .addComponent(openCoverCoverageParser)
            .addComponent(microsoftWindowsEnvironment);
            
        FakesRemover fakesRemover = picoContainer.getComponent(FakesRemover.class);
        fakesRemover.removeFakes(new File(testEnvironment.getTargetDir()));
        
        VsTestEmbeddedInOpenCoverCommandBuilder commandBuilder=picoContainer.getComponent(VsTestEmbeddedInOpenCoverCommandBuilder.class);
        commandBuilder.execute();

    }

    private void getSolution() {
        solution = getMicrosoftWindowsEnvironment().getCurrentSolution();
        if (solution == null) {
            throw new SonarException("No .NET solution or project has been given to the Gallio command builder.");
        }
    }

}
