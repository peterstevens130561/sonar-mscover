/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class CommandHelper {
    private CommandHelper() {
    }
    
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

    public static String parenthesizeArgument(String str) {
        String escapedStr="";
        if(!StringUtils.isEmpty(str) ) {
            escapedStr = "\"" + str + "\"";
        }
        return escapedStr;
    }
}
