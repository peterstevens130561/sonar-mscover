package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.BaseSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.LineSaver;

public class TestsCoverageLineSaver extends LineSaver {

    private int hitDataCalled ;
    private int saveSummaryMeasuresCalled;
    
    public int getHitDataCalls() {
        return hitDataCalled;
    }
    
    public int getSaveSummaryMeasuresCalls() {
        return saveSummaryMeasuresCalled ;
    }
    public TestsCoverageLineSaver(SensorContext context, Project project,CoverageRegistry registry) {
        super(context, project,registry);
    }

    public void saveSummaryMeasures(SensorContext context,
            FileCoverage coverageData, Resource<?> resource) {
        saveSummaryMeasuresCalled++;
    }

    public Measure getHitData(FileCoverage coverable) {
        hitDataCalled++;
        return null;
    }

    public void setResourceFilter(ResourceFilter fileFilter) {       
    }

}
