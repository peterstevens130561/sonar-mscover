package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;

public interface LineCoverageSaverFactory {
    public LineFileCoverageSaver createIntegrationTestSaver();

    public LineFileCoverageSaver createUnitTestSaver();

    public LineFileCoverageSaver createOverallSaver();
}
