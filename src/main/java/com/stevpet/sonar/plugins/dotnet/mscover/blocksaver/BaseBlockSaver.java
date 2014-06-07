package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import java.io.File;

import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;

public class BaseBlockSaver  implements BlockSaver {

    private SourceFileNamesRegistry sourceFileNamesRegistry;
    private FileBlocksRegistry fileBlocksRegistry;
    private BlockMeasureSaver blockMeasureSaver;
    public BaseBlockSaver(BlockMeasureSaver blockMeasureSaver) {
        this.blockMeasureSaver = blockMeasureSaver;
    }


    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockSaver#setSourceFileNamesRegistry(com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry)
     */
    public void setSourceFileNamesRegistry(
            SourceFileNamesRegistry sourceFileNamesRegistry) {
        this.sourceFileNamesRegistry = sourceFileNamesRegistry;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockSaver#setFileBlocksRegistry(com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry)
     */
    public void setFileBlocksRegistry(FileBlocksRegistry fileBlocksRegistry) {
        this.fileBlocksRegistry = fileBlocksRegistry;
        
    }
    

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockSaver#save()
     */
    public void save() {
        for (FileBlocks fileBlocks: fileBlocksRegistry.values()) {
            File sonarFile = getSonarFile(fileBlocks);
            blockMeasureSaver.saveMeasures(fileBlocks, sonarFile);
        }

    }
    
    
    public File getSonarFile(FileBlocks fileBlocks) {
        String fileID = fileBlocks.getFileId();
        String coveragePath = sourceFileNamesRegistry.getSourceFileName(fileID);
        return new File(coveragePath);
    }


    protected double convertPercentage(Number percentage) {
        return ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }


    public static Double getCoverage(BlockModel methodBlock) {
        if(methodBlock.getBlocks()==0) {
            return 100.0;
        }
        return (double) (methodBlock.getCovered()*100.0) / (double) methodBlock.getBlocks();
    }

}
