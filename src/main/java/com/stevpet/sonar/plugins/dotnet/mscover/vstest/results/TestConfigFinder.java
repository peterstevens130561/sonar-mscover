package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jfree.util.Log;

public class TestConfigFinder {
    
    private String configRegex = ".*\\.test(settings)|(runconfig)$";
    private Pattern configPattern= Pattern.compile(configRegex) ;
    
    public String getDefault(File folder) {
        String defaultConfigAbsolutePath=null;
        Log.info("No test configuration file specified in sonar.mscover.vsttest.testsettings, will try to find default");
        for(File file:folder.listFiles()) {
            String fileName = file.getName();
            Matcher matcher = configPattern.matcher(fileName);
            if(matcher.find()) {

                if(defaultConfigAbsolutePath!=null) {
                    Log.warn("found multiple test configuration files, found also " + fileName);
                } else {
                    defaultConfigAbsolutePath=file.getAbsolutePath();
                    Log.info("found test configuration file, will be used " + fileName);
                }
            }
        }
        return defaultConfigAbsolutePath;
    }
}
