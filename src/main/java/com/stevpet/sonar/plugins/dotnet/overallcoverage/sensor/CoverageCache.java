package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public interface CoverageCache {

    SonarCoverage get(Project module);

}
