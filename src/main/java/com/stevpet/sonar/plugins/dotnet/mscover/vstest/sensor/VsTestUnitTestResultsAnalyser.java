package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.ConcreteOpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ConcreteParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class VsTestUnitTestResultsAnalyser {

    static final Logger LOG = LoggerFactory
            .getLogger(VsTestUnitTestResultsAnalyser.class);
    private UnitTestRegistry registry;
    private UnitTestFilesResultRegistry filesResultRegistry;
    private SourceFileNamesRegistry sourceFileNamesRegistry;

    private MethodToSourceFileIdMap map;
    private Project project;
    private OpenCoverParserFactory openCoverParserFactory = new ConcreteOpenCoverParserFactory();
    private ParserFactory factory = new ConcreteParserFactory();
    private MeasureSaver measureSaver;
    private SourceFilePathHelper sourceFilePathHelper ;
    private ResourceMediator resourceMediator;
    
    /**
     * @deprecated Use {@link #UnitTestAnalyser(Project,SensorContext,MeasureSaver,SourceFilePathHelper)} instead
     */
    public VsTestUnitTestResultsAnalyser(Project project, SensorContext context,MeasureSaver measureSaver) {
        this(project, measureSaver, new SourceFilePathHelper(),ResourceMediator.createWithEmptyFilters(context, project));
    }

    public VsTestUnitTestResultsAnalyser(Project project,MeasureSaver measureSaver, SourceFilePathHelper sourceFilePathHelper,ResourceMediator resourceMediator) {
        this.project = project;
        this.measureSaver = measureSaver;
        this.sourceFilePathHelper = sourceFilePathHelper;
        this.resourceMediator = resourceMediator; 
    }
    
    /**
     * Saves the test results, using the coverage-report.xml file generated by OpenCover to
     * link unit tests to source files
     */
    public void analyseOpenCoverTestResults(String coveragePath, String resultsPath) {
        createRegistries(); 
        
        parseUnitTestResultsFile(resultsPath);      
        parseOpenCoverFile(coveragePath);
        
        saveUnitTests();
    }
    /**
     * Saves the test results, using the coverage.xml file generated by vstest to
     * link unit tests to source files
     * @param coveragePath
     * @param resultsPath
     */
    public void analyseVsTestResults(String coveragePath, String resultsPath) {
        createRegistries(); 
        
        parseUnitTestResultsFile(resultsPath);      
        parseCoverageFile(coveragePath);
        
        saveUnitTests();
    }
    private void parseCoverageFile(String coverageFileName) {
        ParserSubject parser = factory.createFileNamesParser(map, sourceFileNamesRegistry);
        
        LOG.info("MSCover Reading " + coverageFileName );
        File coverageFile = new File(coverageFileName);
        parser.parseFile(coverageFile);
    }
    
    private void parseOpenCoverFile(String openCoverFileName) {
       ParserSubject parser = openCoverParserFactory.createOpenCoverFileNamesParser(map, sourceFileNamesRegistry);
        
        File coverageFile = new File(openCoverFileName);
        parser.parseFile(coverageFile);       
    }

    private void saveUnitTests() {

        String projectDirectory = getProjectDirectory(project);
        sourceFilePathHelper.setProjectPath(projectDirectory);
        
        UnitTestResultRegistry unitTestResultRegistry = registry.getResults();
        filesResultRegistry.mapResults(unitTestResultRegistry, map);
       
        TestSaver testSaver = new TestSaver(resourceMediator,measureSaver);

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
