package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import org.sonar.api.utils.text.XmlWriter;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public interface CoverageSerializationService {

    /**
     * Serialize the repository using the xmlWriter 
     * @param file
     * @param projectCoverageRepository
     */
    public void Serialize(XmlWriter xmlWriter, ProjectCoverageRepository projectCoverageRepository);
}
