package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestConfigFinder {
    static final Logger LOG = LoggerFactory
            .getLogger(TestConfigFinder.class);

    private String configRegex = ".*\\.test(settings)|(runconfig)$";
    private Pattern configPattern= Pattern.compile(configRegex) ;
    
    public String getDefault(File folder) {
        String defaultConfigAbsolutePath=null;
        LOG.info("No test configuration file specified in sonar.mscover.vsttest.testsettings, will try to find default");
        for(File file:folder.listFiles()) {
            String fileName = file.getName();
            Matcher matcher = configPattern.matcher(fileName);
            if(matcher.find()) {

                if(defaultConfigAbsolutePath!=null) {
                    LOG.warn("found multiple test configuration files, found also " + fileName);
                } else {
                    defaultConfigAbsolutePath=file.getAbsolutePath();
                    LOG.info("found test configuration file, will be used " + fileName);
                }
            }
        }
        return defaultConfigAbsolutePath;
    }
    
    public File findDefaultUpwards(File folder) {
        return findFileUpwards(folder,configRegex);
    }
    public File findFileUpwards(File folder, String regex) {
        boolean found=false;
        Pattern pattern = Pattern.compile(regex);
        while(folder.length() >0) {
            File file=getFileName(folder,pattern); 
            if(file !=null) {
                return file;
            }
            folder = folder.getParentFile();
        }
        return null;
    }

    private File getFileName(File folder,Pattern pattern) {
        for(File file:folder.listFiles()) {
            String fileName = file.getName();
            Matcher matcher = pattern.matcher(fileName);
            if(matcher.find()) {
                    LOG.info("found test configuration file, will be used " + fileName);
                    return file;
            }
        }
        return null;
    }
}
