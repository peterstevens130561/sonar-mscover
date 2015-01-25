package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import org.apache.commons.lang.StringUtils;



public class ParserData {
    private String skip;
    private int skipLevel = 0;
    private int level=0;
    

    
    /**
     * go next level down in the xml tree
     */
    public void levelDown() {
        ++level;
    }
    
    /**
     * go level up in the xml tree
     */
    public void levelUp() {
        --level;
    }
    
    public void setSkipThisLevel() {
        this.skipLevel=level;
    }
    

    public boolean parseLevelAndBelow()  {
        boolean skip=skipLevel >0 && level >= skipLevel;
        if(!skip) {
            skipLevel=0;
        }
        return skip;
    }

    public Object getLevel() {
        return level;
    }


}
