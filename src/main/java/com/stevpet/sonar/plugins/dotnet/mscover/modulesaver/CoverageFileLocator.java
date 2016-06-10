package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;

public interface CoverageFileLocator {


    File getArtifactCoverageFile(File root, String projectName, String artifactName);

}