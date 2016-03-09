package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

@InstantiationStrategy(value=InstantiationStrategy.PER_BATCH)
public class DefaultCoverageCache implements CoverageCache,BatchExtension {
    private final Logger LOG = LoggerFactory.getLogger(DefaultCoverageCache.class);
    private Map<String, SonarCoverage> map = new HashMap<>();

    @Override
    public SonarCoverage get(String module) {
        LOG.info("get {}",module);
        return map.get(module);
    }

    @Override
    public void merge(SonarCoverage coverage, String module) {
        LOG.info("merging {}",module);
        if (!map.containsKey(module)){
            map.put(module, coverage);
        } else {
            SonarCoverage dest = map.get(module);
            dest.merge(coverage);

        }
    }

}