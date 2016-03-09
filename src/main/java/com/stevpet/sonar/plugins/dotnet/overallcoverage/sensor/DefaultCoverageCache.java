package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;

import java.util.HashMap;
import java.util.Map;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

@InstantiationStrategy(value=InstantiationStrategy.PER_BATCH)
public class DefaultCoverageCache implements CoverageCache,BatchExtension {

    private Map<String, SonarCoverage> map = new HashMap<>();

    @Override
    public SonarCoverage get(String module) {
        return map.get(module);
    }

    @Override
    public void merge(SonarCoverage coverage, String key) {
        if (!map.containsKey(key)) {
            map.put(key, coverage);
        } else {
            SonarCoverage dest = map.get(key);
            dest.merge(coverage);

        }
    }

}
