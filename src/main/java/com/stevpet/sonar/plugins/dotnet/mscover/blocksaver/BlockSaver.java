package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.Saver;

public interface BlockSaver extends Saver {

    public abstract void setSourceFileNamesRegistry(
            SourceFileNamesRegistry sourceFileNamesRegistry);

    public abstract void setFileBlocksRegistry(
            FileBlocksRegistry fileBlocksRegistry);

    
}