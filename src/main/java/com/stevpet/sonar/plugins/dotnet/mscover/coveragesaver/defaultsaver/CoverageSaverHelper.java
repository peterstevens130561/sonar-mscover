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
     * 
     * @param coveragePoints
     * @param metric
     *            - a linhits metric
     * @return the measure
     */

    private interface PointCoverage {
        int data(CoverageLinePoint point);
    }

    Measure<?> getCoveredHitData(CoverageLinePoints coveragePoints, Metric<?> metric) {

        return getLineHitData(coveragePoints, metric, point -> point.getCovered());
    }

    Measure<?> getToCoverHitData(CoverageLinePoints coveragePoints, Metric<?> metric) {
        return getLineHitData(coveragePoints, metric, point -> point.getToCover());
    }

    Measure<?> getLineHitData(CoverageLinePoints coveragePoints, Metric<?> metric, PointCoverage pointCoverage) {
        PropertiesBuilder<Integer, Integer> propertiesBuilder = new PropertiesBuilder<Integer, Integer>(metric);
        coveragePoints.getPoints().forEach(point -> propertiesBuilder.add(point.getLine(), pointCoverage.data(point)));
        return propertiesBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
    }

}
