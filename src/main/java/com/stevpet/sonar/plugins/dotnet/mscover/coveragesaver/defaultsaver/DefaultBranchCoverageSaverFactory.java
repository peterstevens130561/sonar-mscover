package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;

import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultBranchCoverageSaverFactory {
    private final ResourceResolver resourceResolver;

    public DefaultBranchCoverageSaverFactory(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }
    public BranchFileCoverageSaver createIntegrationTestBranchSaver() {
        return new DefaultBranchCoverageSaver(resourceResolver,new IntegrationTestBranchCoverageMetrics());
    }
    
    public BranchFileCoverageSaver createUnitTestBranchSaver() {
        return new DefaultBranchCoverageSaver(resourceResolver,new UnitTestBranchCoverageMetrics());       
    }
    
    public BranchFileCoverageSaver createOverallBranchSaver() {        
        return new DefaultBranchCoverageSaver(resourceResolver,new OverallBranchCoverageMetrics());       
    }
}
