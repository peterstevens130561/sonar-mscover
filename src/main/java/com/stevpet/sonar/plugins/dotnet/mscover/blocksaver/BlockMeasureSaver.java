package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;

public interface BlockMeasureSaver {

    public void saveMeasures(FileBlocks fileBlocks, File sonarFile);

}