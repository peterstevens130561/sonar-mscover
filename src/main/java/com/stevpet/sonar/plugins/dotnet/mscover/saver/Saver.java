package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;

public interface Saver {

    /**
     * Save the registry into sonarQube
     */
    void save();
    void saveSummaryMeasures(SensorContext context,
            FileCoverage coverageData, Resource<?> resource) ;
    


    Measure getHitData(FileCoverage coverable);
    void setDateFilter(DateFilter dateFilter);
    void setResourceFilter(ResourceFilter fileFilter);
   
}