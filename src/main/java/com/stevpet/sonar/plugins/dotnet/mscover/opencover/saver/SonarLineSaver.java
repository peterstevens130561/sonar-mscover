package com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver;

import java.io.File;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverageSummary;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class SonarLineSaver implements FileCoverageSaver {

    private MeasureSaver measureSaver;

    private SonarLineSaver(MeasureSaver measureSaver) {
        this.measureSaver = measureSaver ;
    }
    
    public static FileCoverageSaver create(MeasureSaver measureSaver) {
        return new SonarLineSaver(measureSaver);
    }
    private final PropertiesBuilder<String, Integer> lineHitsBuilder = new PropertiesBuilder<String, Integer>(
            CoreMetrics.COVERAGE_LINE_HITS_DATA);
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover.FileCoverageSaver#saveMeasures(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints, java.io.File)
     */
    public void saveMeasures(
            CoverageLinePoints coveragePoints, File file) {
        measureSaver.setFile(file);
        SonarCoverageSummary summary=coveragePoints.getSummary();
        double coverage = summary.getCoverage();
        measureSaver.setIgnoreTwiceSameMeasure();
        measureSaver.saveFileMeasure(CoreMetrics.LINES, (double) summary.getToCover());
        measureSaver.setExceptionOnTwiceSameMeasure();
        measureSaver.saveFileMeasure( CoreMetrics.LINES_TO_COVER, (double) summary.getToCover());
        measureSaver.saveFileMeasure( CoreMetrics.UNCOVERED_LINES, (double)summary.getToCover() -summary.getCovered());
        measureSaver.saveFileMeasure( CoreMetrics.COVERAGE, convertPercentage(coverage));
        measureSaver.saveFileMeasure( CoreMetrics.LINE_COVERAGE, convertPercentage(coverage));
        Measure lineMeasures=getHitData(coveragePoints);
        measureSaver.saveFileMeasure(lineMeasures);
    }

    /*
     * Generates a measure that contains the visits of each line of the source
     * file.
     */
    public Measure getHitData(CoverageLinePoints coveragePoints) {
        PropertiesBuilder<String, Integer> hitsBuilder =  lineHitsBuilder;

        hitsBuilder.clear();
        for (CoveragePoint point : coveragePoints.getPoints()) {
            int lineNumber = ((SonarLinePoint) point).getLine();
            int countVisits = point.getCovered();
            hitsBuilder.add(Integer.toString(lineNumber), countVisits);
        }
        return hitsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
    }
    
    protected double convertPercentage(Number percentage) {
        return ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }
    

}
