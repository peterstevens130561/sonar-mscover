package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.DotNetConstants;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.plugin.Extension;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageAnalyser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.UnitTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

@DependsUpon(DotNetConstants.CORE_PLUGIN_EXECUTED)
public class ResultsSensor implements Sensor {
    static final Logger LOG = LoggerFactory
            .getLogger(ResultsSensor.class);
    private PropertiesHelper propertiesHelper ;
    private UnitTestRunner unitTestRunner;
    private TimeMachine timeMachine;
    private ModuleFileSystem moduleFileSystem;
    private VisualStudioSolution vsSolution;
    private VisualStudioProject vsProject;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private VsTestEnvironment vsTestEnvironment;
   
    
    public ResultsSensor(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,Settings settings,TimeMachine timeMachine,ModuleFileSystem moduleFileSystem,VsTestEnvironment vsTestEnvironment) {
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment;

        this.timeMachine = timeMachine;
        propertiesHelper = new PropertiesHelper(settings);
        unitTestRunner = UnitTestRunner.create();
        unitTestRunner.setPropertiesHelper(propertiesHelper);
        this.moduleFileSystem=moduleFileSystem;
        this.vsTestEnvironment = vsTestEnvironment;
    }
    
    /**
     * tests whether the sensor should execute on the project
     */
    public boolean shouldExecuteOnProject(Project project) {
        String resultsPath=propertiesHelper.getUnitTestResultsPath();
        boolean shouldExecute = (StringUtils.isNotEmpty(resultsPath) || unitTestRunner.shouldRun()) && (project.isRoot() == propertiesHelper.excuteRoot());
        LOG.info("ResultsSensor {}",shouldExecute);
        return shouldExecute;
    }

    public void analyse(Project project, SensorContext context) {
        LOG.info("MsCover Starting analysing test results");
        String coveragePath;
        String resultsPath;
        if(unitTestRunner.shouldRun()) {
            /*
            LOG.info("MsCover Running tests");
            File projectDirectory = getSolutionDirectory(project);
            unitTestRunner.setSolutionDirectory(projectDirectory);
            unitTestRunner.runTests();
            resultsPath=unitTestRunner.getResultsXmlPath();
            coveragePath=unitTestRunner.getOutputPath();
            */
            resultsPath=vsTestEnvironment.getXmlResultsPath();
            coveragePath=vsTestEnvironment.getXmlCoveragePath();
        } else {
            LOG.info("MsCover using test results");
            coveragePath = propertiesHelper.getUnitTestCoveragePath();
            resultsPath=propertiesHelper.getUnitTestResultsPath();
        }
        UnitTestAnalyser analyser = new UnitTestAnalyser(project,context);
        analyser.analyseResults(coveragePath, resultsPath);
        if(unitTestRunner.shouldRun()) {
            CoverageAnalyser coverageAnalyser = new CoverageAnalyser(project,context, timeMachine, propertiesHelper);
            coverageAnalyser.analyseResults(coveragePath);
        }
    }

 
    private File getSolutionDirectory(Project project) {
  
        File projectDirectory = null;
        if(project.isRoot()) {
            projectDirectory=moduleFileSystem.baseDir();
        } else {
            VisualStudioSolution solution = microsoftWindowsEnvironment.getCurrentSolution();
            projectDirectory= solution.getSolutionDir();
        }
        return projectDirectory;
    }

    

}
