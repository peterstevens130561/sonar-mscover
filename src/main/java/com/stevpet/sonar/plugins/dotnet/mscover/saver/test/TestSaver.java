package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.utils.ParsingUtils;
import org.sonar.api.utils.SonarException;

import com.google.common.base.CharMatcher;
import com.google.common.io.Files;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.AlwaysPassThroughDateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestFileResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry.ForEachTest;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.BaseSaver;

public class TestSaver extends  BaseSaver {

    private static final Logger LOG = LoggerFactory
            .getLogger(TestSaver.class);
    
    private final SensorContext context;
    private final Project project;
    
    private SourceFileNamesRegistry sourceFileNamesRegistry ;
    private UnitTestFilesResultRegistry unitTestFilesResultRegistry;
    
    public TestSaver(SensorContext context, Project project) {
        super(context,project);
        this.project= project;
        this.context = context;
    }


    public void save() {
        unitTestFilesResultRegistry.forEachTest(new SaveTestMeasures());

    }

    class SaveTestMeasures implements ForEachTest {
        
        public void execute(String fileID, UnitTestFileResultModel fileResults) {
        String sourceFileName=sourceFileNamesRegistry.getSourceFileName(fileID);
        File sourceFile=new File(sourceFileName);
        org.sonar.api.resources.File sonarFile = getSonarFileResource(sourceFile);
        if(sonarFile==null) {
            return;
        }
        LOG.debug("- Saving coverage information for file {}",
                sonarFile.getKey());
        saveFileMeasures(fileResults, sonarFile);
        saveTestMeasures(fileResults, sonarFile);
    }
   
}

    public void saveTestMeasures( UnitTestFileResultModel fileResults,
            org.sonar.api.resources.File sonarFile) {
        context.saveMeasure(sonarFile, CoreMetrics.TEST_SUCCESS_DENSITY,fileResults.getDensity());
        context.saveMeasure(sonarFile, CoreMetrics.TEST_FAILURES,fileResults.getFail());
        context.saveMeasure(sonarFile,CoreMetrics.TESTS,fileResults.getTests());  
    }

    public void saveFileMeasures( UnitTestFileResultModel fileResults,
            org.sonar.api.resources.File sonarFile) {
        // TODO Auto-generated method stub
        
    }
}