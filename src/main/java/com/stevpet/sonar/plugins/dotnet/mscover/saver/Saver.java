package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.IOException;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;

public interface Saver {

    /**
     * Save the registry into sonarQube
     * @throws IOException 
     */
    void save() throws IOException;
    void setDateFilter(DateFilter dateFilter);
    void setResourceFilter(ResourceFilter fileFilter);
}