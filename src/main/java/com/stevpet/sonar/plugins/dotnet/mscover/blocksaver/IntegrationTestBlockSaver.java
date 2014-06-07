package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import java.io.File;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class IntegrationTestBlockSaver implements BlockMeasureSaver {
    
    private MeasureSaver measureSaver ;
    private IntegrationTestBlockSaver(MeasureSaver measureSaver) {
        this.measureSaver = measureSaver ;
    }
    
    public static IntegrationTestBlockSaver create(MeasureSaver measureSaver) {
        return new IntegrationTestBlockSaver(measureSaver);
    }

    

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockMeasureSaver#saveSummaryMeasures(org.sonar.api.batch.SensorContext, com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks, org.sonar.api.resources.Resource)
     */
    public void saveMeasures(FileBlocks fileBlocks,
            File file) {
        measureSaver.setFile(file);
        BlockModel methodBlock=fileBlocks.getSummaryBlock();

        measureSaver.saveFileMeasure(CoreMetrics.IT_UNCOVERED_CONDITIONS,(double) methodBlock.getNotCovered());
        measureSaver.saveFileMeasure(CoreMetrics.IT_CONDITIONS_TO_COVER,(double)methodBlock.getBlocks());
        measureSaver.saveFileMeasure(CoreMetrics.IT_BRANCH_COVERAGE,BaseBlockSaver.getCoverage(methodBlock));

         PropertiesBuilder<String, Integer> lineConditionsBuilder = new PropertiesBuilder<String, Integer>(
                CoreMetrics.IT_CONDITIONS_BY_LINE);
        PropertiesBuilder<String, Integer> lineCoveredConditionsBuilder = new PropertiesBuilder<String, Integer>(
                CoreMetrics.IT_COVERED_CONDITIONS_BY_LINE);
        lineConditionsBuilder.clear();
        lineCoveredConditionsBuilder.clear();
        for (BlockModel methodBlocks : fileBlocks.getBlocks()) {
            int lineNumber = methodBlocks.getLine();
            int covered =methodBlocks.getCovered();
            lineConditionsBuilder.add(Integer.toString(lineNumber), methodBlocks.getBlocks());
            lineCoveredConditionsBuilder.add(Integer.toString(lineNumber), covered);
        }
        Measure lineConditionsMeasure= lineConditionsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
        measureSaver.saveFileMeasure(lineConditionsMeasure);
        Measure lineCoveredConditionsMeasure=lineCoveredConditionsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
        measureSaver.saveFileMeasure(lineCoveredConditionsMeasure);
    }

}
