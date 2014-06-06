package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;


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


public class UnitTestBlockSaver implements BlockMeasureSaver {

    private final PropertiesBuilder<String, Integer> lineConditionsBuilder = new PropertiesBuilder<String, Integer>(
            CoreMetrics.CONDITIONS_BY_LINE);
    private final PropertiesBuilder<String, Integer> lineCoveredConditionsBuilder = new PropertiesBuilder<String, Integer>(
            CoreMetrics.COVERED_CONDITIONS_BY_LINE);
    
    
    public void saveSummaryMeasures(SensorContext context, FileBlocks fileBlocks,
            Resource<?> resource) {

        BlockModel methodBlock=fileBlocks.getSummaryBlock();
        context.saveMeasure(resource,CoreMetrics.UNCOVERED_CONDITIONS,(double) methodBlock.getNotCovered());
        context.saveMeasure(resource, CoreMetrics.CONDITIONS_TO_COVER,(double)methodBlock.getBlocks());
        context.saveMeasure(resource, CoreMetrics.BRANCH_COVERAGE,BaseBlockSaver.getCoverage(methodBlock));
    }


    public void saveLineMeasures(SensorContext context, FileBlocks fileMethodBlocks,Resource<?> resource) {
        lineConditionsBuilder.clear();
        lineCoveredConditionsBuilder.clear();
        for (BlockModel methodBlocks : fileMethodBlocks.getBlocks()) {
            int lineNumber = methodBlocks.getLine();
            int covered =methodBlocks.getCovered();
            lineConditionsBuilder.add(Integer.toString(lineNumber), methodBlocks.getBlocks());
            lineCoveredConditionsBuilder.add(Integer.toString(lineNumber), covered);
        }
        Measure lineConditionsMeasure= lineConditionsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
        context.saveMeasure(resource,lineConditionsMeasure);
        Measure lineCoveredConditionsMeasure=lineCoveredConditionsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
        context.saveMeasure(resource,lineCoveredConditionsMeasure);
    }

}

