package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class CommandHelper {
    /**
     * Put parenthesis around each of the elements i.e. 'joa Jewel' results in '"joa Jewel"'
     * @param elements
     * @return
     */
    public static List<String> parenthesizeArguments(
            List<String> elements) {
        List<String> parenthesized = new ArrayList<String>();
        if(elements==null) {
            return parenthesized;
        }
        for (String str : elements) {
            if(!StringUtils.isEmpty(str) ) {
            String escapedPath = "\"" + str + "\"";
            parenthesized.add(escapedPath);
            }
        }
        return parenthesized;
    }
}
