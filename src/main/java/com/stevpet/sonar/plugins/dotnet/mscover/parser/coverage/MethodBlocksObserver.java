package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;

public class MethodBlocksObserver implements ParserObserver {


    private final Pattern pattern;
    private BlockModel block = new BlockModel();
    private FileBlocksRegistry registry;
    private boolean isActive = false;
    public MethodBlocksObserver() {
        pattern = Pattern
                .compile("Module/NamespaceTable/Class/Method/((Blocks(Not)?Covered)|(Lines/SourceFileID)|(Lines/LnStart))");
    }

    public void setRegistry(FileBlocksRegistry registry) {
        this.registry = registry;
    }

    public boolean isMatch(String path) {
        Matcher matcher = pattern.matcher(path);
        return matcher.matches();
    }

    public void observe(String name, String text) {
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
