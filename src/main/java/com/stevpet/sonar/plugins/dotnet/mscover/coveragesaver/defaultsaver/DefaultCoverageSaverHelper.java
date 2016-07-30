package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;

/**
 * to be used by the CoverageSavers
 *
 */
public class DefaultCoverageSaverHelper implements CoverageSaverHelper{

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverHelper#getCoveredHitData(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints, org.sonar.api.measures.Metric)
     */
    @Override
    public Measure<?> getCoveredHitData(CoverageLinePoints coveragePoints, Metric<?> metric) {
        return getLineHitData(coveragePoints, metric, point -> point.getCovered());
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverHelper#getToCoverHitData(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints, org.sonar.api.measures.Metric)
     */
    @Override
    public Measure<?> getToCoverHitData(CoverageLinePoints coveragePoints, Metric<?> metric) {
        return getLineHitData(coveragePoints, metric, point -> point.getToCover());
    }

    private interface Strategy {
        int data(CoverageLinePoint point);
    }
    
    private Measure<?> getLineHitData(CoverageLinePoints coveragePoints, Metric<?> metric, Strategy strategy) {
        PropertiesBuilder<Integer, Integer> propertiesBuilder = new PropertiesBuilder<Integer, Integer>(metric);
        coveragePoints.getPoints()
        .forEach(point -> propertiesBuilder
                .add(
                        point.getLine(), 
                        strategy.data(point)
                )
        );
        return propertiesBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
    }

    @Override
    public double convertPercentage(Number percentage) {
        return  ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }

}
