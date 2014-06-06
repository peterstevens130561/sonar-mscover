package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;


import java.io.File;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.UnitTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;


public class UnitTestBlockSaver implements BlockMeasureSaver {

    private MeasureSaver measureSaver;
    
    private final PropertiesBuilder<String, Integer> lineConditionsBuilder = new PropertiesBuilder<String, Integer>(
            CoreMetrics.CONDITIONS_BY_LINE);
    private final PropertiesBuilder<String, Integer> lineCoveredConditionsBuilder = new PropertiesBuilder<String, Integer>(
            CoreMetrics.COVERED_CONDITIONS_BY_LINE);
    
    private UnitTestBlockSaver(MeasureSaver measureSaver) {
        this.measureSaver = measureSaver ;
    }
    
    public static UnitTestBlockSaver create(MeasureSaver measureSaver) {
        return new UnitTestBlockSaver(measureSaver);
    }
    

    public void saveMeasures(SensorContext context, FileBlocks fileBlocks,
            File file) {
        measureSaver.setFile(file);
        BlockModel methodBlock=fileBlocks.getSummaryBlock();
        measureSaver.saveMeasure(CoreMetrics.UNCOVERED_CONDITIONS,(double) methodBlock.getNotCovered());
        measureSaver.saveMeasure(CoreMetrics.CONDITIONS_TO_COVER,(double)methodBlock.getBlocks());
        measureSaver.saveMeasure(CoreMetrics.BRANCH_COVERAGE,BaseBlockSaver.getCoverage(methodBlock));

        lineConditionsBuilder.clear();
        lineCoveredConditionsBuilder.clear();
        for (BlockModel methodBlocks : fileBlocks.getBlocks()) {
            int lineNumber = methodBlocks.getLine();
            int covered =methodBlocks.getCovered();
            lineConditionsBuilder.add(Integer.toString(lineNumber), methodBlocks.getBlocks());
            lineCoveredConditionsBuilder.add(Integer.toString(lineNumber), covered);
        }
        Measure lineConditionsMeasure= lineConditionsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
        measureSaver.saveMeasure(lineConditionsMeasure);
        Measure lineCoveredConditionsMeasure=lineCoveredConditionsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
        measureSaver.saveMeasure(lineCoveredConditionsMeasure);
    }

}

