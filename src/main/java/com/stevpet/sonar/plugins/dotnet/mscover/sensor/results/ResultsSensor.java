package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.model.ResultsModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.ResultsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.ResultsParserSubject;

public class ResultsSensor implements Sensor {
    static final Logger LOG = LoggerFactory
            .getLogger(ResultsSensor.class);
    Settings settings;
    PropertiesHelper propertiesHelper ;
    String resultsPath;
    public ResultsSensor(Settings settings) {
        this.settings = settings;
        propertiesHelper = new PropertiesHelper(settings);
        resultsPath=propertiesHelper.getIntegrationTestsResults();
        LOG.info("ResultsSensor:{}",resultsPath);
    }
    
    public boolean shouldExecuteOnProject(Project project) {
        return StringUtils.isNotEmpty(resultsPath);
    }

    public void analyse(Project project, SensorContext context) {
        ResultsObserver resultsObserver = new ResultsObserver();
        ResultsModel resultsModel = new ResultsModel() ;
        resultsObserver.setRegistry(resultsModel);
        
        ParserSubject parser = new ResultsParserSubject();
        parser.registerObserver(resultsObserver);
        
        File file = new File(resultsPath);
        if(!file.exists()) {
            throw new SonarException("Can't open " + resultsPath );
        }
        parser.parseFile(file);
        LOG.info("ResultsSensor: {}",resultsModel.getExecutedTests());
        context.saveMeasure(CoreMetrics.TESTS,(double)resultsModel.getExecutedTests());
        context.saveMeasure(CoreMetrics.TEST_FAILURES,(double)resultsModel.getFailedTests());
        context.saveMeasure(CoreMetrics.TEST_ERRORS,(double)resultsModel.getErroredTests());
    }

}
