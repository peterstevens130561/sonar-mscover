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
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestSaver;

public class UnitTestAnalyser {

    static final Logger LOG = LoggerFactory
            .getLogger(UnitTestAnalyser.class);
    private UnitTestResultRegistry unitTestRegistry;
    private ResultsModel resultsModel;
    private UnitTestFilesResultRegistry filesResultRegistry;
    private SourceFileNamesRegistry sourceFileNamesRegistry;
    
    private ResultsObserver unitTestSummaryResultsObserver;
    private UnitTestResultObserver unitTestResultObserver;
    private UnitTestObserver unitTestObserver;
    private MethodObserver methodObserver;
    private MethodToSourceFileIdMap map;
    private SourceFileNamesObserver sourceFileNamesObserver;
    private SensorContext context;
    private Project project;
    
    
    public UnitTestAnalyser(Project project, SensorContext context) {
        this.project = project;
        this.context = context;
    }
    
    public void analyseResults(String coveragePath, String resultsPath) {
        createRegistries(); 
        createObservers();
        wireRegistriesIntoObservers();
        
        parseUnitTestResultsFile(resultsPath);      
        parseCoverageFile(coveragePath);
        
        saveSummaryTestResults();
        saveUnitTests();
    }
    private void parseCoverageFile(String coverageFileName) {
        CoverageParserSubject coverageParser = new CoverageParserSubject();
        registerCoverageObservers(coverageParser);
        
        LOG.info("MSCover Reading " + coverageFileName );
        File coverageFile = new File(coverageFileName);
        coverageParser.parseFile(coverageFile);
    }

    private void saveUnitTests() {
        SourceFilePathHelper sourceFilePathHelper = new SourceFilePathHelper();
        String projectDirectory = getProjectDirectory(project);
        sourceFilePathHelper.setProjectPath(projectDirectory);
        
        filesResultRegistry.mapResults(unitTestRegistry, map);
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
        LOG.info("ResultsSensor: {}",resultsModel.getExecutedTests());
        context.saveMeasure(CoreMetrics.TESTS,(double)resultsModel.getExecutedTests());
        context.saveMeasure(CoreMetrics.TEST_FAILURES,(double)resultsModel.getFailedTests());
        context.saveMeasure(CoreMetrics.TEST_ERRORS,(double)resultsModel.getErroredTests());
    }

    private void parseUnitTestResultsFile(String resultsPath) {
        ParserSubject resultsParser = new ResultsParserSubject();    
        registerResultsObservers(resultsParser);
        
        File file = new File(resultsPath);
        if(!file.exists()) {
            throw new SonarException("Can't open " + resultsPath );
        }
        // just the totals
        resultsParser.parseFile(file);
    }

    private void createRegistries() {
        resultsModel = new ResultsModel();
        unitTestRegistry = new UnitTestResultRegistry();
        map = new MethodToSourceFileIdMap();
        filesResultRegistry = new UnitTestFilesResultRegistry();
        sourceFileNamesRegistry = new SourceFileNamesRegistry();
    }
    
    private void createObservers() {
        unitTestSummaryResultsObserver = new ResultsObserver();
        unitTestResultObserver = new UnitTestResultObserver();
        unitTestObserver = new UnitTestObserver();
        methodObserver = new MethodObserver();
        sourceFileNamesObserver = new SourceFileNamesObserver();
    }
    
    private void registerCoverageObservers(CoverageParserSubject coverageParser) {
        coverageParser.registerObserver(methodObserver);
        coverageParser.registerObserver(sourceFileNamesObserver);
    }

    private void registerResultsObservers(ParserSubject resultsParser) {
        resultsParser.registerObserver(unitTestResultObserver);
        resultsParser.registerObserver(unitTestObserver);
        resultsParser.registerObserver(unitTestSummaryResultsObserver);
    }

    private void wireRegistriesIntoObservers() {
        unitTestSummaryResultsObserver.setRegistry(resultsModel);
        unitTestResultObserver.setRegistry(unitTestRegistry);
        unitTestObserver.setRegistry(unitTestRegistry);
        methodObserver.setRegistry(map);
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
    }


}
