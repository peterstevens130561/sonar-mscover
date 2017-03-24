package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;

public interface CoverageFileSerializer {


    void SourceFile(String absolutePath, List<LineToCover> getLinesToCover);

    void start();

    void end();

}
