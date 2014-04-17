package com.stevpet.sonar.plugins.dotnet.mscover.saver.line;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceLine;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;

public class IntegrationTestLineSaver extends LineSaver {
    private static final Logger LOG = LoggerFactory
            .getLogger(IntegrationTestLineSaver.class);
    
    private final PropertiesBuilder<String, Integer> lineHitsBuilder = new PropertiesBuilder<String, Integer>(
            CoreMetrics.IT_COVERAGE_LINE_HITS_DATA);
    
    public IntegrationTestLineSaver(SensorContext context,
            Project project, CoverageRegistry registry) {
        super(context, project, registry);
    }
    
    public void saveSummaryMeasures(SensorContext context,
            FileCoverage coverageData, Resource<?> resource) {
        double coverage = coverageData.getCoverage();
        LOG.debug("MsCover resource       " + resource.getKey());
        LOG.debug("MsCover coverage       " + coverage);
        LOG.debug("MsCover lines to cover " + coverageData.getCountLines());
        LOG.debug("MsCover covered lines  " + coverageData.getCoveredLines());
        context.saveMeasure(resource, CoreMetrics.IT_LINES_TO_COVER,
                (double) coverageData.getCountLines());

        context.saveMeasure(
                resource,
                CoreMetrics.IT_UNCOVERED_LINES,
                (double) coverageData.getCountLines()
                        - coverageData.getCoveredLines());
        context.saveMeasure(resource, CoreMetrics.IT_COVERAGE,
                convertPercentage(coverage));
        context.saveMeasure(resource, CoreMetrics.IT_LINE_COVERAGE,
                convertPercentage(coverage));
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
}
