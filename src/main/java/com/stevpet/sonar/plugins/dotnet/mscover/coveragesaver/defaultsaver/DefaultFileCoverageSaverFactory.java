package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.FileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultFileCoverageSaverFactory {
    private final ResourceResolver resourceResolver;

    public DefaultFileCoverageSaverFactory(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }
    public BranchFileCoverageSaver createIntegrationTestBranchSaver() {
        return new ItemCoverageSaverBase(resourceResolver,new IntegrationTestBranchCoverageMetrics());
    }
    
    public BranchFileCoverageSaver createUnitTestBranchSaver() {
        return new ItemCoverageSaverBase(resourceResolver,new UnitTestBranchCoverageMetrics());       
    }
    
    public BranchFileCoverageSaver createOverallBranchSaver() {        
        return new ItemCoverageSaverBase(resourceResolver,new OverallBranchCoverageMetrics());       
    }
}
