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
