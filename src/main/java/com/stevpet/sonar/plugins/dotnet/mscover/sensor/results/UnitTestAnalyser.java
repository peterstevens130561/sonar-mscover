package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.model.ResultsModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ConcreteParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.MethodObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.SourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.ResultsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.ResultsParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.UnitTestObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.UnitTestResultObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class UnitTestAnalyser {

    static final Logger LOG = LoggerFactory
            .getLogger(UnitTestAnalyser.class);
    private UnitTestRegistry registry;
    private UnitTestFilesResultRegistry filesResultRegistry;
    private SourceFileNamesRegistry sourceFileNamesRegistry;

    private MethodToSourceFileIdMap map;
    private SensorContext context;
    private Project project;
    private ParserFactory factory = new ConcreteParserFactory();
    private MeasureSaver measureSaver;
    
    
    public UnitTestAnalyser(Project project, SensorContext context,MeasureSaver measureSaver) {
        this.project = project;
        this.context = context;
        this.measureSaver = measureSaver;
    }
    
    public void analyseResults(String coveragePath, String resultsPath) {
        createRegistries(); 
        
        parseUnitTestResultsFile(resultsPath);      
        parseCoverageFile(coveragePath);
        
        saveSummaryTestResults();
        saveUnitTests();
    }
    private void parseCoverageFile(String coverageFileName) {
        ParserSubject parser = factory.createFileNamesParser(map, sourceFileNamesRegistry);
        
        LOG.info("MSCover Reading " + coverageFileName );
        File coverageFile = new File(coverageFileName);
        parser.parseFile(coverageFile);
    }

    private void saveUnitTests() {
        SourceFilePathHelper sourceFilePathHelper = new SourceFilePathHelper();
        String projectDirectory = getProjectDirectory(project);
        sourceFilePathHelper.setProjectPath(projectDirectory);
        
        UnitTestResultRegistry unitTestResultRegistry = registry.getResults();
        filesResultRegistry.mapResults(unitTestResultRegistry, map);
        ResourceMediator resourceMediator = ResourceMediator.createWithEmptyFilters(context, project);        
        TestSaver testSaver = new TestSaver(context,resourceMediator);

        testSaver.setUnitTestFilesResultRegistry(filesResultRegistry);
        testSaver.setSourceFileNamesRegistry(sourceFileNamesRegistry);
        testSaver.setSourceFilePathHelper(sourceFilePathHelper);
        LOG.info("MsCover Saving results");
        testSaver.save();
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

    private void saveSummaryTestResults() {
        ResultsModel resultsModel = registry.getSummary();
        LOG.info("ResultsSensor: {}",resultsModel.getExecutedTests());
        measureSaver.saveSummaryMeasure(CoreMetrics.TESTS,(double)resultsModel.getExecutedTests());
        measureSaver.saveSummaryMeasure(CoreMetrics.TEST_FAILURES,(double)resultsModel.getFailedTests());
        measureSaver.saveSummaryMeasure(CoreMetrics.TEST_ERRORS,(double)resultsModel.getErroredTests());
    }

    private void parseUnitTestResultsFile(String resultsPath) {

        ParserSubject resultsParser = factory.createUnitTestResultsParser(registry); 
        
        File file = new File(resultsPath);
        if(!file.exists()) {
            throw new SonarException("Can't open " + resultsPath );
        }
        resultsParser.parseFile(file);
    }

    private void createRegistries() {
        registry = new UnitTestRegistry();
        map = new MethodToSourceFileIdMap();
        filesResultRegistry = new UnitTestFilesResultRegistry();
        sourceFileNamesRegistry = new SourceFileNamesRegistry();
    }
    
}
