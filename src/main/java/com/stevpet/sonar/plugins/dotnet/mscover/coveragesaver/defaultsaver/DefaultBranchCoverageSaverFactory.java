package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.FileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultBranchCoverageSaverFactory {
    private final ResourceResolver resourceResolver;
    private CoverageSaverHelper coverageSaverHelper;

    public DefaultBranchCoverageSaverFactory(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        this.coverageSaverHelper = new DefaultCoverageSaverHelper();
    }
    public BranchFileCoverageSaver createIntegrationTestBranchSaver() {
        return new DefaultBranchCoverageSaver(resourceResolver,new IntegrationTestBranchCoverageMetrics(),coverageSaverHelper);
    }
    
    public BranchFileCoverageSaver createUnitTestBranchSaver() {
        return new DefaultBranchCoverageSaver(resourceResolver,new UnitTestBranchCoverageMetrics(), coverageSaverHelper);       
    }
    
    public BranchFileCoverageSaver createOverallBranchSaver() {        
        return new DefaultBranchCoverageSaver(resourceResolver,new OverallBranchCoverageMetrics(),coverageSaverHelper);       
    }
}
