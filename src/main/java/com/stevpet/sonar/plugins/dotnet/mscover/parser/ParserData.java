package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import org.apache.commons.lang.StringUtils;



public class ParserData {
    private String skip;

    public void skipTillNextElement(String name) {
        skip=name;
    }
    public boolean shouldSkip(String name) {
        if(StringUtils.isEmpty(skip)) {
            return false;
        }
        boolean result = !name.equalsIgnoreCase(skip);
        if(!result) {
            skip="";
        }
        return result;
    }
}
