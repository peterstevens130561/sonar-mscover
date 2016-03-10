package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class IntegrationTestBranchFileCoverageSaver extends DefaultBranchCoverageSaver implements
		BranchFileCoverageSaver {


	IntegrationTestBranchFileCoverageSaver(ResourceResolver resourceResolver) {
	    super(resourceResolver,new IntegrationTestBranchCoverageMetrics());
    }

}
