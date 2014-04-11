package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;

public class MethodBlocksObserver extends BaseParserObserver {


    private BlockModel block = new BlockModel();
    private FileBlocksRegistry registry;
    private boolean isActive = false;
    public MethodBlocksObserver() {
        setPattern("Module/NamespaceTable/Class/Method/((Blocks(Not)?Covered)|(Lines/SourceFileID)|(Lines/LnStart))");
    }

    public void setRegistry(FileBlocksRegistry registry) {
        this.registry = registry;
    }

    public void observeElement(String name, String text) {
        if (name.equals(block.getFirstElement())) {
            block = new BlockModel();
            isActive=true;
        }
        if(!isActive) {
            return ;
        }
        block.setField(name,text);
        
        if(name.equals(block.getLastElement())) {
            registry.add(block.getFileID(), block);
            isActive=false;
            block= new BlockModel();
            return;
        }
               
    }

}
