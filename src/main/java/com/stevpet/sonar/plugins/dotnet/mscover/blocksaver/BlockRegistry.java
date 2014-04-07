package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;

    public interface BlockRegistry {

        void setSourceFileNamesRegistry(SourceFileNamesRegistry sourceFileNamesRegistry);
        void setFileBlocksRegistry(FileBlocksRegistry fileBlocksRegistry);
    }
