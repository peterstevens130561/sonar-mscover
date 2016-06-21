package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;


import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarLinePoint;

/**
 * Should ultimately contain all logic for the coverage savers
 * 
 * @author stevpet
 *
 */
public class CoverageSaverHelper {

    /**
     * given the coveragepoints, and metric create the measure
     * @param coveragePoints
     * @param metric - a linhits metric
     * @return the measure
     */
    Measure<?> getHitData( CoverageLinePoints coveragePoints,Metric<?> metric) {
        PropertiesBuilder<Integer, Integer> hitsBuilder =  new PropertiesBuilder<Integer, Integer>( metric);
    
        hitsBuilder.clear();
        for (CoverageLinePoint point : coveragePoints.getPoints()) {
            int lineNumber = ((SonarLinePoint) point).getLine();
            int countVisits = point.getCovered();
            hitsBuilder.add(lineNumber, countVisits);
        }
        return hitsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
    }

    Measure<?> getCoveredHitData(CoverageLinePoints coveragePoints, Metric<?> coveredMetric) {
        PropertiesBuilder<Integer, Integer> lineCoveredConditionsBuilder = new PropertiesBuilder<Integer, Integer>(
                coveredMetric);
        lineCoveredConditionsBuilder.clear();
        for (CoverageLinePoint linePoint : coveragePoints.getPoints()) {
            int lineNumber = linePoint.getLine();
            lineCoveredConditionsBuilder.add(lineNumber, linePoint.getCovered());
        }
        Measure lineCoveredConditionsMeasure=lineCoveredConditionsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
        return lineCoveredConditionsMeasure;
    }

    Measure<?> getToCoverHitData(Metric<?> toCoverMetric, CoverageLinePoints coveragePoints) {
        PropertiesBuilder<Integer, Integer> lineConditionsBuilder = new PropertiesBuilder<Integer, Integer>(
                toCoverMetric);
        lineConditionsBuilder.clear();
        for (CoverageLinePoint linePoint : coveragePoints.getPoints()) {
            int lineNumber = linePoint.getLine();
            lineConditionsBuilder.add(lineNumber, linePoint.getToCover());
        }
        Measure lineConditionsMeasure= lineConditionsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
        return lineConditionsMeasure;
    }


}
