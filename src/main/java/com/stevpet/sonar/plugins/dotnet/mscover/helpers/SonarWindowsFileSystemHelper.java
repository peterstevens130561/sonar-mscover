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
package com.stevpet.sonar.plugins.dotnet.mscover.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SonarWindowsFileSystemHelper {
    public static String makeWindowsPath(String path) {
        if(path == null) {
            throw new IllegalArgumentException("path null");
        }
        return path.replaceAll("/", "\\\\");
    }
    
    public static String createQualifiedPath(String path) {
        String qualifiedPath ;
        if(path == null) {
            throw new IllegalArgumentException("path null");
        }
        if(path.contains(" ")) {
            qualifiedPath = "\"" + path + "\"" ;
        } else {
            qualifiedPath = path;
        }
        return qualifiedPath;
    }

    public static void dieOnInvalidPath(String path) {
        Pattern invalidPathChars = Pattern.compile("[^a-zA-z0-9-_ .:/\\\\]");
        Matcher invalidPathCharsMatcher = invalidPathChars.matcher(path);
        if (invalidPathCharsMatcher.find()) {
            throw new IllegalArgumentException("path '" + path + "' has invalid characters ");
        }
    }
}
