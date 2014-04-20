package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestFileResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry.ForEachUnitTestFile;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.BaseSaver;

public class TestSaver extends  BaseSaver {

    private static final Logger LOG = LoggerFactory
            .getLogger(TestSaver.class);
    
    private final SensorContext context;
    private SourceFileNamesRegistry sourceFileNamesRegistry ;
    private UnitTestFilesResultRegistry unitTestFilesResultRegistry;
    
    public SourceFileNamesRegistry getSourceFileNamesRegistry() {
        return sourceFileNamesRegistry;
    }

    public void setSourceFileNamesRegistry(
            SourceFileNamesRegistry sourceFileNamesRegistry) {
        this.sourceFileNamesRegistry = sourceFileNamesRegistry;
    }

    public UnitTestFilesResultRegistry getUnitTestFilesResultRegistry() {
        return unitTestFilesResultRegistry;
    }

    public void setUnitTestFilesResultRegistry(
            UnitTestFilesResultRegistry unitTestFilesResultRegistry) {
        this.unitTestFilesResultRegistry = unitTestFilesResultRegistry;
    }


    
    public TestSaver(SensorContext context, Project project) {
        super(context,project);
        this.context = context;
    }


    public void save() {
        unitTestFilesResultRegistry.forEachTest(new SaveUnitTestFileMeasures());

    }

    class SaveUnitTestFileMeasures implements ForEachUnitTestFile {
        
        public void execute(String fileID, UnitTestFileResultModel fileResults) {
        org.sonar.api.resources.File sonarFile = tryToGetUnitTestResource(fileID);
        if(sonarFile==null) {
            return;
        }
        LOG.debug("- Saving coverage information for file {}",
                sonarFile.getKey());

        saveSummaryMeasures(fileResults, sonarFile);
        saveTestCaseMeasures(fileResults, sonarFile);

    }

        private org.sonar.api.resources.File tryToGetUnitTestResource(
                String fileID) {
            String sourceFileName=sourceFileNamesRegistry.getSourceFileName(fileID);
            File sourceFile=new File(sourceFileName);
            try {
                String canonicalName=sourceFile.getCanonicalPath();
                sourceFile=new File(canonicalName);
            } catch (IOException e) {
                String msg = "IOException for" + sourceFileName;
                LOG.error(msg + e);
                throw new SonarException(msg,e);
            }

            org.sonar.api.resources.File sonarFile = getSonarFileResource(sourceFile);
            return sonarFile;
        }
   
}

    public void saveSummaryMeasures( UnitTestFileResultModel fileResults,
            org.sonar.api.resources.File sonarFile) {
        context.saveMeasure(sonarFile,CoreMetrics.SKIPPED_TESTS, (double)0);
        context.saveMeasure(sonarFile, CoreMetrics.TEST_ERRORS,(double) 0);
        context.saveMeasure(sonarFile, CoreMetrics.TEST_SUCCESS_DENSITY,fileResults.getDensity()*100.0);
        context.saveMeasure(sonarFile, CoreMetrics.TEST_FAILURES,fileResults.getFail());
        context.saveMeasure(sonarFile, CoreMetrics.TEST_EXECUTION_TIME,1000.0);
        context.saveMeasure(sonarFile,CoreMetrics.TESTS,fileResults.getTests());  
    }

    public void saveTestCaseMeasures( UnitTestFileResultModel fileResults,
            org.sonar.api.resources.File sonarFile) {
        StringBuilder testCaseDetails = new StringBuilder(256);
        testCaseDetails.append("<tests-details>");
        List<UnitTestResultModel> details = fileResults.getUnitTests();
        for (UnitTestResultModel detail : details) {
          testCaseDetails.append("<testcase status=\"ok\"");
          testCaseDetails.append(" time=\"0\"");
          testCaseDetails.append(" name=\"");
          testCaseDetails.append(detail.getTestName());
          testCaseDetails.append("\"");
          testCaseDetails.append(" />");
        }
        testCaseDetails.append("</tests-details>");
        String data=testCaseDetails.toString();
        Measure testData = new Measure(CoreMetrics.TEST_DATA, data);
        testData.setPersistenceMode(PersistenceMode.DATABASE);
        context.saveMeasure(sonarFile, testData);
        LOG.debug("test detail : {}", testCaseDetails);
    }
}