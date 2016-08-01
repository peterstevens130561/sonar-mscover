package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;

import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultBranchCoverageSaverFactory implements BranchCoverageSaverFactory {
    private final ResourceResolver resourceResolver;
    private final CoverageSaverHelper coverageSaverHelper;

    public DefaultBranchCoverageSaverFactory(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        this.coverageSaverHelper = new DefaultCoverageSaverHelper();
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.BranchCoverageSaverFactory#createIntegrationTestBranchSaver()
     */
    @Override
    public BranchFileCoverageSaver createIntegrationTestSaver() {
        return new DefaultBranchCoverageSaver(resourceResolver,new IntegrationTestBranchCoverageMetrics(),coverageSaverHelper);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.BranchCoverageSaverFactory#createUnitTestBranchSaver()
     */
    @Override
    public BranchFileCoverageSaver createUnitTestSaver() {
        return new DefaultBranchCoverageSaver(resourceResolver,new UnitTestBranchCoverageMetrics(), coverageSaverHelper);       
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.BranchCoverageSaverFactory#createOverallBranchSaver()
     */
    @Override
    public BranchFileCoverageSaver createOverallSaver() {        
        return new DefaultBranchCoverageSaver(resourceResolver,new OverallBranchCoverageMetrics(),coverageSaverHelper);       
    }
}
