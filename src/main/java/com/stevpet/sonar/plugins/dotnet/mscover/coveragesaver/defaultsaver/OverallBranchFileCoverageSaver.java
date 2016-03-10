package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class OverallBranchFileCoverageSaver extends ItemCoverageSaverBase implements
        BranchFileCoverageSaver {

    OverallBranchFileCoverageSaver(ResourceResolver resourceResolver) {
        super(resourceResolver,new OverallBranchCoverageMetrics());
    }

 
}