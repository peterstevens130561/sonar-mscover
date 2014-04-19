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
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.model.ResultsModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.MethodObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.SourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.ResultsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.ResultsParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.UnitTestObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.UnitTestResultObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.plugin.Extension;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.Saver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestSaver;

@Extension
public class ResultsSensor implements Sensor {
    static final Logger LOG = LoggerFactory
            .getLogger(ResultsSensor.class);
    Settings settings;
    PropertiesHelper propertiesHelper ;
    String resultsPath;
    private Object sourceFileNamesRegistry;
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
        LOG.info("MsCover Starting analysing test results");
        ResultsModel resultsModel = new ResultsModel() ;
        UnitTestResultRegistry unitTestRegistry = new UnitTestResultRegistry();
        
        ResultsObserver resultsObserver = new ResultsObserver();
        resultsObserver.setRegistry(resultsModel);
       
        ParserSubject resultsParser = new ResultsParserSubject();    
        UnitTestResultObserver unitTestResultObserver = new UnitTestResultObserver();

        unitTestResultObserver.setRegistry(unitTestRegistry);
        resultsParser.registerObserver(unitTestResultObserver);
        
        UnitTestObserver unitTestObserver = new UnitTestObserver();
        unitTestObserver.setRegistry(unitTestRegistry);
        resultsParser.registerObserver(unitTestObserver);
        resultsParser.registerObserver(resultsObserver);
        
        File file = new File(resultsPath);
        if(!file.exists()) {
            throw new SonarException("Can't open " + resultsPath );
        }
        // just the totals
        resultsParser.parseFile(file);
        
        LOG.info("ResultsSensor: {}",resultsModel.getExecutedTests());
        context.saveMeasure(CoreMetrics.TESTS,(double)resultsModel.getExecutedTests());
        context.saveMeasure(CoreMetrics.TEST_FAILURES,(double)resultsModel.getFailedTests());
        context.saveMeasure(CoreMetrics.TEST_ERRORS,(double)resultsModel.getErroredTests());

        UnitTestFilesResultRegistry filesResultRegistry = new UnitTestFilesResultRegistry();
        
        MethodToSourceFileIdMap map = new MethodToSourceFileIdMap() ;
        MethodObserver methodObserver = new MethodObserver();
        methodObserver.setRegistry(map);
        //TODO: add lines to parse the coverage file
        CoverageParserSubject coverageParser = new CoverageParserSubject();
        coverageParser.registerObserver(methodObserver);
        
        SourceFileNamesRegistry sourceFileNamesRegistry = new SourceFileNamesRegistry();
        SourceFileNamesObserver sourceFileNamesObserver = new SourceFileNamesObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
        coverageParser.registerObserver(sourceFileNamesObserver);
        
        String coverageFileName = propertiesHelper.getUnitTestsPath();
        LOG.info("MSCover Reading " + coverageFileName );
        File coverageFile = new File(coverageFileName);
        coverageParser.parseFile(coverageFile);
        
        methodObserver.setRegistry(map);
        filesResultRegistry.mapResults(unitTestRegistry, map);


        TestSaver testSaver = new TestSaver(context, project);
        testSaver.setResourceFilter(ResourceFilterFactory.createEmptyFilter());
        testSaver.setDateFilter(DateFilterFactory.createEmptyDateFilter());
        testSaver.setUnitTestFilesResultRegistry(filesResultRegistry);
        testSaver.setSourceFileNamesRegistry(sourceFileNamesRegistry);
        
        LOG.info("MsCover Saving results");
        testSaver.save();
        
    }

}
