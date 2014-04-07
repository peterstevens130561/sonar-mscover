package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import java.util.Map;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.plugins.csharp.gallio.TestMetrics;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceLine;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;

public class UnitTestBlockSaver extends BaseBlockSaver {

    private final PropertiesBuilder<String, Integer> lineHitsBuilder = new PropertiesBuilder<String, Integer>(
            CoreMetrics.COVERAGE_LINE_HITS_DATA);
    
    public UnitTestBlockSaver(SensorContext context,
            Project project, CoverageRegistry registry) {
        super(context, project, registry);
    }
    
    public void saveSummaryMeasures(SensorContext context,
            FileCoverage coverageData, Resource<?> resource) {
        double coverage = coverageData.getCoverage();
        context.saveMeasure(resource, TestMetrics.ELOC, (double) coverageData.getCountLines());
        context.saveMeasure(resource, CoreMetrics.LINES_TO_COVER, (double) coverageData.getCountLines()); /*
        context.saveMeasure(resource, CoreMetrics.UNCOVERED_LINES, (double) coverageData.getCountLines() - coverageData.getCoveredLines());
        context.saveMeasure(resource, CoreMetrics.COVERAGE, convertPercentage(coverage));
        context.saveMeasure(resource, CoreMetrics.LINE_COVERAGE, convertPercentage(coverage)); */
    }

    /*
     * Generates a measure that contains the visits of each line of the source
     * file.
     */
    public Measure getHitData(FileCoverage coverable) {
        PropertiesBuilder<String, Integer> hitsBuilder =  lineHitsBuilder;

        hitsBuilder.clear();
        Map<Integer, SourceLine> lines = coverable.getLines();
        for (SourceLine line : lines.values()) {
            int lineNumber = line.getLineNumber();
            int countVisits = line.getCountVisits();
            hitsBuilder.add(Integer.toString(lineNumber), countVisits);
        }
        return hitsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
    }

    @Override
    void saveSummaryMeasures(SensorContext context, FileBlocks fileBlocks,
            Resource<?> sonarFile) {
        // TODO Auto-generated method stub
        
    }

    @Override
    void saveLineMeasures(SensorContext context, FileBlocks fileBlocks,
            Resource<?> sonarFile) {
        // Create line  mesasure
        
    }
}

