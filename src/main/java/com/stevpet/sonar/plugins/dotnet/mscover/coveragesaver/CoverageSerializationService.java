package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import javax.xml.stream.XMLStreamWriter;


import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public interface CoverageSerializationService {

    /**
     * Serialize the repository using the xmlStreamWriter to the generic coverage
     * format specified by sonarsource since 5.6
     * @param xmlStreamWriter a fully initialized XMLStreamWriter
     * @param projectCoverageRepository a valid (nonull) repository
     */
    public void Serialize(XMLStreamWriter xmlStreamWriter, ProjectCoverageRepository projectCoverageRepository);
}
