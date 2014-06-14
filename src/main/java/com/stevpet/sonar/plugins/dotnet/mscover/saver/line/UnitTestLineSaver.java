package com.stevpet.sonar.plugins.dotnet.mscover.saver.line;

import java.io.File;
import java.util.Map;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceLine;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class UnitTestLineSaver implements LineMeasureSaver {

    private MeasureSaver measureSaver;


    private UnitTestLineSaver(MeasureSaver measureSaver) {
        this.measureSaver = measureSaver ;
    }
    
    public static UnitTestLineSaver create(MeasureSaver measureSaver) {
        return new UnitTestLineSaver(measureSaver);
    }
    private final PropertiesBuilder<String, Integer> lineHitsBuilder = new PropertiesBuilder<String, Integer>(
            CoreMetrics.COVERAGE_LINE_HITS_DATA);
    

    public void saveMeasures(
            FileCoverage coverageData, File file) {
        measureSaver.setFile(file);
        double coverage = coverageData.getCoverage();
        measureSaver.setIgnoreTwiceSameMeasure();
        measureSaver.saveFileMeasure(CoreMetrics.LINES, (double) coverageData.getCountLines());
        measureSaver.setExceptionOnTwiceSameMeasure();
        measureSaver.saveFileMeasure( CoreMetrics.LINES_TO_COVER, (double) coverageData.getCountLines());
        measureSaver.saveFileMeasure( CoreMetrics.UNCOVERED_LINES, (double) coverageData.getCountLines() - coverageData.getCoveredLines());
        measureSaver.saveFileMeasure( CoreMetrics.COVERAGE, convertPercentage(coverage));
        measureSaver.saveFileMeasure( CoreMetrics.LINE_COVERAGE, convertPercentage(coverage));
        Measure lineMeasures=getHitData(coverageData);
        measureSaver.saveFileMeasure(lineMeasures);
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
    
    protected double convertPercentage(Number percentage) {
        return ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }

}

