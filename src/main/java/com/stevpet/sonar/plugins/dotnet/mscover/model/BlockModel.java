package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.util.List;


public class BlockModel extends Model {
    
 
    private static final String SOURCE_FILE_ID_ELEMENTNAME = "SourceFileID";
    private static final String LN_START_ELEMENT = "LnStart";
    private static final String BLOCKS_NOT_COVERED_ELEMENT = "BlocksNotCovered";
    private static final String BLOCKS_COVERED_ELEMENT = "BlocksCovered";
    private int covered;
    private int notCovered;
    private int line;
    private String fileID;
    
    public int getCovered() {
        return covered;
    }

    public int getNotCovered() {
        return notCovered;
    }

    public int getLine() {
        return line;
    }

    public void setCovered(String text) {
        setCovered(Integer.parseInt(text));
    }
    public void setNotCovered(String text) {
        setNotCovered(Integer.parseInt(text));
    }
    
    public void setLine(String text) {
        setLine(Integer.parseInt(text));
    }
    
    public void setCovered(int value) {
        covered=value;
    }
    
    public void setNotCovered(int value) {
        notCovered=value;
    }
    
    public void setLine(int value) {
        line=value;
    }
    
    public void setField(String name, String text) {
        if (name.equals(BLOCKS_COVERED_ELEMENT)) {
            setCovered(text);
        }
        if (name.equals(BLOCKS_NOT_COVERED_ELEMENT)) {
            setNotCovered(text);
        }
        if(name.equals(SOURCE_FILE_ID_ELEMENTNAME)) {
            setFileID(text);
        }
        if (name.equals(LN_START_ELEMENT)) {
            setLine(text);
        }
    }


    private void setFileID(String text) {
        fileID=text;
        
    }

    public String getFileID() {
        return fileID;
    }
    
    public String getFirstElement() {
        return BLOCKS_COVERED_ELEMENT;
    }
    
    public String getLastElement() {
        return SOURCE_FILE_ID_ELEMENTNAME;
    }
    
    public void addBlocks(List<BlockModel> blocks) {
        for(BlockModel block: blocks) {
            addBlock(block);
        }   
    } 
    
    public void addBlock(BlockModel block) {
        covered += block.getCovered();
        notCovered += block.getNotCovered();
    }

    public int getBlocks() {
        return getCovered() + getNotCovered();
    }


}
