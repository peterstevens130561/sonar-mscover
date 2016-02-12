package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;

public interface CoverageSaverFactory {

    /**
     * saver for unit test coverage data created with vstest (only lines)
     * @return 
     */
    CoverageSaver createVsTestUnitTestCoverageSaver();

    /**
     * saver for unit test coverage data created with opencover (lines and branches)
     */
    CoverageSaver createOpenCoverUnitTestCoverageSaver();

    /**
     * saver for integration test coverage data created with opencover (lines and branches)
     */
    CoverageSaver createOpenCoverIntegrationTestCoverageSaver();

}