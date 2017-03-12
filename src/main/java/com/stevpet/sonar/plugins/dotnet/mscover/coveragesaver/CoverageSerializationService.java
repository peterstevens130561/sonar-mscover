package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public interface CoverageSerializationService {

    /**
     * Serialize the repository into the file. 
     * @param file
     * @param projectCoverageRepository
     */
    public void Serialize(File file, ProjectCoverageRepository projectCoverageRepository);
}
