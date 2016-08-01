package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;

public interface BranchCoverageSaverFactory {

    public BranchFileCoverageSaver createIntegrationTestSaver();

    public BranchFileCoverageSaver createUnitTestSaver();

    public BranchFileCoverageSaver createOverallSaver();

}