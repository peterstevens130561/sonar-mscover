package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultLineCoverageSaverFactory implements LineCoverageSaverFactory {
    private final ResourceResolver resourceResolver;
    private final CoverageSaverHelper coverageSaverHelper;
    
    public DefaultLineCoverageSaverFactory(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        this.coverageSaverHelper = new DefaultCoverageSaverHelper();
    }
    @Override
    public LineFileCoverageSaver createIntegrationTestSaver() {
        return new DefaultLineFileCoverageSaver(new IntegrationTestLineCoverageMetrics(), resourceResolver, coverageSaverHelper);
    }

    @Override
    public LineFileCoverageSaver createUnitTestSaver() {
        return new DefaultLineFileCoverageSaver(new UnitTestLineCoverageMetrics(), resourceResolver, coverageSaverHelper);
    }

    @Override
    public LineFileCoverageSaver createOverallSaver() {
        return new DefaultLineFileCoverageSaver(new OverallLineCoverageMetrics(), resourceResolver, coverageSaverHelper);
    }

}
