package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.util.ArrayList;
import java.util.List;

public class FileBlocks {
    private String fileID ;
    List<BlockModel> blocks = new ArrayList<BlockModel>() ;
    
    public void add(BlockModel block) {
        blocks.add(block);
    }

    public String getFileId() {
        return fileID;
    }

    public void setFileId(String fileId) {
        this.fileID = fileId;
    }

    public List<BlockModel> getBlocks() {
        return blocks;
    }
    
    public BlockModel getSummaryBlock() {
        BlockModel summaryBlock = new BlockModel();
        summaryBlock.addBlocks(blocks);
        return summaryBlock;
    }
}
