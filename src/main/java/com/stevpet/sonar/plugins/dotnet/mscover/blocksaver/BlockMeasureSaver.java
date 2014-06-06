package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;

public interface BlockMeasureSaver {

    public abstract void saveSummaryMeasures(SensorContext context,
            FileBlocks fileBlocks, Resource<?> resource);

    /*
     * Generates a measure that contains the visits of each line of the source
     * file.
     */
    public abstract void saveLineMeasures(SensorContext context,
            FileBlocks fileMethodBlocks, Resource<?> resource);

}