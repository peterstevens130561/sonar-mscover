package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.plugin.Extension;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageAnalyser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.UnitTestRunner;

@Extension
public class ResultsSensor implements Sensor {
    static final Logger LOG = LoggerFactory
            .getLogger(ResultsSensor.class);
    private PropertiesHelper propertiesHelper ;
    private UnitTestRunner unitTestRunner;
    private TimeMachine timeMachine;
   
    
    public ResultsSensor(Settings settings,TimeMachine timeMachine) {
        this.timeMachine = timeMachine;
        propertiesHelper = new PropertiesHelper(settings);
        unitTestRunner = UnitTestRunner.create();
        unitTestRunner.setPropertiesHelper(propertiesHelper);
    }
    
    public boolean shouldExecuteOnProject(Project project) {
        String resultsPath=propertiesHelper.getUnitTestResultsPath();
        boolean shouldExecute = (StringUtils.isNotEmpty(resultsPath) || unitTestRunner.shouldRun()) && project.isRoot();
        LOG.info("ResultsSensor {}",shouldExecute);
        return shouldExecute;
    }

    public void analyse(Project project, SensorContext context) {
        LOG.info("MsCover Starting analysing test results");
        String coveragePath;
        String resultsPath;
        if(unitTestRunner.shouldRun()) {
            LOG.info("MsCover Running tests");
            String projectDirectory = getProjectDirectory(project);
            unitTestRunner.setSolutionDirectory(new File(projectDirectory));
            unitTestRunner.runTests();
            resultsPath=unitTestRunner.getResultsPath();
            coveragePath=unitTestRunner.getOutputPath();
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

 
    private String getProjectDirectory(Project project) {
        String projectDirectory = null;
        try {
            projectDirectory = project.getFileSystem().getBasedir().getCanonicalPath();
        } catch (IOException e) {
            String msg="Could not get path to project directory";
            LOG.error(msg);
            throw new SonarException(msg,e);
        }
        return projectDirectory;
    }

    

}
