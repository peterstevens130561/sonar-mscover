package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import java.io.File;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;

public interface BlockMeasureSaver {

    public abstract void saveMeasures(SensorContext context,
            FileBlocks fileBlocks, File sonarFile);

}