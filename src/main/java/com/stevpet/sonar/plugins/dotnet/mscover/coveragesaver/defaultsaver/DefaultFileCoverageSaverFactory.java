package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.FileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultFileCoverageSaverFactory {
    private final ResourceResolver resourceResolver;

    public DefaultFileCoverageSaverFactory(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }
    public FileCoverageSaver createIntegrationTestBranchSaver() {
        return new ItemCoverageSaverBase(resourceResolver,new IntegrationTestBranchCoverageMetrics());
    }
    
    public FileCoverageSaver createUnitTestBranchSaver() {
        return new ItemCoverageSaverBase(resourceResolver,new UnitTestBranchCoverageMetrics());       
    }
    
    public FileCoverageSaver createOverallBranchSaver() {        
        return new ItemCoverageSaverBase(resourceResolver,new OverallBranchCoverageMetrics());       
    }
}
