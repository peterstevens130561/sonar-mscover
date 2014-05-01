package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;

public class IntegrationTestBlockSaver extends BaseBlockSaver {
    private static final Logger LOG = LoggerFactory
            .getLogger(IntegrationTestBlockSaver.class);
    
   
    
    public IntegrationTestBlockSaver(SensorContext context,
            Project project) {
        super(context, project);
    }
    
    @Override
    public void saveSummaryMeasures(SensorContext context, FileBlocks fileBlocks,
            Resource<?> resource) {
        BlockModel methodBlock=fileBlocks.getSummaryBlock();

        LOG.debug("MsCover resource       " + resource.getKey());
        LOG.debug("MsCover coverage       " + getCoverage(methodBlock));
        LOG.debug("MsCover lines to cover " + methodBlock.getBlocks());
        LOG.debug("MsCover covered lines  " + methodBlock.getCovered());

        context.saveMeasure(resource,CoreMetrics.IT_UNCOVERED_CONDITIONS,(double) methodBlock.getNotCovered());
        context.saveMeasure(resource, CoreMetrics.IT_CONDITIONS_TO_COVER,(double)methodBlock.getBlocks());
        context.saveMeasure(resource, CoreMetrics.IT_BRANCH_COVERAGE,getCoverage(methodBlock));
    }

    /*
     * Generates a measure that contains the visits of each line of the source
     * file.
     */
    @Override
    public void saveLineMeasures(SensorContext context, FileBlocks fileMethodBlocks,Resource<?> resource) {
         PropertiesBuilder<String, Integer> lineConditionsBuilder = new PropertiesBuilder<String, Integer>(
                CoreMetrics.IT_CONDITIONS_BY_LINE);
        PropertiesBuilder<String, Integer> lineCoveredConditionsBuilder = new PropertiesBuilder<String, Integer>(
                CoreMetrics.IT_COVERED_CONDITIONS_BY_LINE);
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
