package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.plugin.Extension;

@Extension
public class ResultsSensor implements Sensor {
    static final Logger LOG = LoggerFactory
            .getLogger(ResultsSensor.class);
    Settings settings;
    PropertiesHelper propertiesHelper ;
   
    
    public ResultsSensor(Settings settings) {
        this.settings = settings;
        propertiesHelper = new PropertiesHelper(settings);
    }
    
    public boolean shouldExecuteOnProject(Project project) {
        String resultsPath=propertiesHelper.getUnitTestResultsPath();
        boolean shouldExecute = StringUtils.isNotEmpty(resultsPath);
        LOG.info("ResultsSensor {},shouldExecute");
        return shouldExecute;
    }

    public void analyse(Project project, SensorContext context) {
        LOG.info("MsCover Starting analysing test results");
        String coveragePath = propertiesHelper.getUnitTestCoveragePath();
        String resultsPath=propertiesHelper.getUnitTestResultsPath();
        UnitTestAnalyser analyser = new UnitTestAnalyser(project,context);
        analyser.analyseResults(coveragePath, resultsPath);
    }

 


    

}
