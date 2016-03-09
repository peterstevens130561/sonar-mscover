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
    public SonarCoverage get(String moduleName) {
        LOG.info("get {}",moduleName);
        return map.get(moduleName);
    }

    @Override
    public void merge(SonarCoverage coverage, String moduleName) {
        LOG.info("merging {}",moduleName);
        if (!map.containsKey(moduleName)){
            map.put(moduleName, coverage);
        } else {
            SonarCoverage dest = map.get(moduleName);
            dest.merge(coverage);

        }
    }

    @Override
    public void delete(String moduleName) {
        map.remove(moduleName);
    }
    
    

}