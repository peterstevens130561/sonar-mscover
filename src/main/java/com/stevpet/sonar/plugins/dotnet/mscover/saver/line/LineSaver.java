package com.stevpet.sonar.plugins.dotnet.mscover.saver.line;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.AlwaysPassThroughDateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.BaseSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.Saver;

public abstract class LineSaver implements Saver, LineMeasureSaver {

    private static final Logger LOG = LoggerFactory
            .getLogger(LineSaver.class);
    protected final SensorContext sensorContext;
    protected final Project project;
    protected CoverageRegistry registry ;
    protected DateFilter dateFilter = new AlwaysPassThroughDateFilter();
    protected ResourceFilter resourceFilter = ResourceFilterFactory.createEmptyFilter();
    private ResourceMediator resourceMediator;
    
    public LineSaver(SensorContext context, Project project,
            CoverageRegistry registry) {
        this.project= project;
        this.sensorContext = context;
        this.registry = registry;
        resourceMediator = ResourceMediator.create(sensorContext, project);
    }

    public LineSaver(SensorContext sensorContext, Project project) {
        this.project= project;
        this.sensorContext = sensorContext;
        resourceMediator = ResourceMediator.create(sensorContext, project);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.CoverageSaver#save()
     */
    public void save() {

        for (FileCoverage fileCoverage : registry.getFileCoverages()) {
            org.sonar.api.resources.File sonarFile = getSonarFile(fileCoverage);
            if(sonarFile==null) {
                continue;
            }
            LOG.debug("- Saving coverage information for file {}",
                    sonarFile.getKey());
            saveSummaryMeasures(sensorContext, fileCoverage, sonarFile);
            saveLineMeasures(sensorContext,fileCoverage, sonarFile);
        }

    }
    
    public void setDateFilter(DateFilter dateFilter) {
        resourceMediator.setDateFilter(dateFilter);
        
    }

    public void setResourceFilter(ResourceFilter fileFilter) {
        resourceMediator.setResourceFilter(fileFilter);
        
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.saver.line.LineMeasureSaver#saveSummaryMeasures(org.sonar.api.batch.SensorContext, com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage, org.sonar.api.resources.Resource)
     */
    public abstract void saveSummaryMeasures(SensorContext context,
            FileCoverage coverageData, Resource resource) ;
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.saver.line.LineMeasureSaver#saveLineMeasures(org.sonar.api.batch.SensorContext, com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage, org.sonar.api.resources.File)
     */
    public void saveLineMeasures(SensorContext context,FileCoverage fileCoverage,
            org.sonar.api.resources.File sonarFile) {
        context.saveMeasure(sonarFile,getHitData(fileCoverage));
        
    }
   
    protected double convertPercentage(Number percentage) {
        return ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }
    
    public org.sonar.api.resources.File getSonarFile(FileCoverage fileCoverage) {
        File file = fileCoverage.getFile();
        if (file == null) {
            return null;
        }
        return resourceMediator.getSonarFileResource(file);
    }
    
    public void saveLineMeasures(FileCoverage fileCoverage,
            org.sonar.api.resources.File sonarFile) {
        sensorContext.saveMeasure(sonarFile, getHitData(fileCoverage));
    }
    
    public abstract Measure getHitData(FileCoverage coverable);
    
}
