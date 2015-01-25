package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import org.h2.util.StringUtils;

public class ParserData {
    private String skip;

    public void skipTillNextElement(String name) {
        skip=name;
    }
    public boolean shouldSkip(String name) {
        if(StringUtils.isNullOrEmpty(skip)) {
            return false;
        }
        boolean result = !name.equalsIgnoreCase(skip);
        if(!result) {
            skip="";
        }
        return result;
    }
}
