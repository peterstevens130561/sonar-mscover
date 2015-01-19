package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ResultsModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestFileResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry.ForEachUnitTestFile;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.NullResource;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class TrxTestSaver implements TestSaver {

    static final Logger LOG = LoggerFactory
            .getLogger(TrxTestSaver.class);
    
    private TestResultsSaver testResultsSaver  ;
    private SourceFileNamesRegistry sourceFileNamesRegistry ;
    private UnitTestFilesResultRegistry unitTestFilesResultRegistry;
    private SourceFilePathHelper sourceFilePathHelper;
    ResultsModel  projectSummaryResults;
    MeasureSaver measureSaver;
    private ResourceMediator resourceMediator;
    

    public TrxTestSaver(ResourceMediator resourceMediator,MeasureSaver measureSaver) {
        this.resourceMediator = resourceMediator;
        this.measureSaver = measureSaver;
        testResultsSaver = new TestResultsSaver(measureSaver);
    }
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


    public void setSourceFilePathHelper(SourceFilePathHelper sourceFilePathHelper) {
        this.sourceFilePathHelper = sourceFilePathHelper;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestSaver#save()
     */
    public void save() {
        projectSummaryResults = new ResultsModel();
        unitTestFilesResultRegistry.forEachUnitTestFile(new SaveUnitTestFileMeasures());

    }

    class SaveUnitTestFileMeasures implements ForEachUnitTestFile {

        public void execute(String fileID, UnitTestFileResultModel fileResults) {
        ResourceSeam sonarFile = tryToGetUnitTestResource(fileID);
        if(!(sonarFile instanceof NullResource)) {
            projectSummaryResults.add(fileResults);
        }
        testResultsSaver.saveTestCaseMeasures(fileResults, sonarFile);

    }

        private ResourceSeam tryToGetUnitTestResource(
                String fileID) {
            String sourceFileName=sourceFileNamesRegistry.getSourceFileName(fileID);

            File sourceFile = sourceFilePathHelper.getCanonicalFile(sourceFileName);
            if(sourceFile == null) {
                LOG.warn("Could not get unit test file for file "+sourceFileName);
                return new NullResource();
            }
            return resourceMediator.getSonarTestResource(sourceFile);
        }
   
    }
}