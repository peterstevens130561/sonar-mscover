package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BaseBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;


public class TestBlockSaver extends BaseBlockSaver {

    public TestBlockSaver(SensorContext context, Project project) {
        super(context, project);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void saveSummaryMeasures(SensorContext context,
            FileBlocks fileBlocks, Resource<?> sonarFile) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveLineMeasures(SensorContext context, FileBlocks fileBlocks,
            Resource<?> sonarFile) {
        // TODO Auto-generated method stub
        
    }

}
