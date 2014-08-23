package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;


public class VsTestConfigFinder implements TestConfigFinder {
    static final Logger LOG = LoggerFactory
            .getLogger(VsTestConfigFinder.class);

    private String configRegex = ".*\\.test(settings)|(runconfig)$";
    private Pattern configPattern= Pattern.compile(configRegex) ;

    private File solutionDirectory;
    
    public VsTestConfigFinder(File solutionDirectory) {
        this.solutionDirectory=solutionDirectory;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.TestConfigFinder#getTestSettingsFileOrDie(java.lang.String)
     */
    public File getTestSettingsFileOrDie(String testSettings) {
        String msg;
        File testSettingsFile;
        if(StringUtils.isEmpty(testSettings)) {
            msg=PropertiesHelper.MSCOVER_TESTSETTINGS + " not set, and no testsettings file found";
            testSettingsFile= findDefaultFileUpwards(solutionDirectory);

        } else {
            msg=PropertiesHelper.MSCOVER_TESTSETTINGS + "=" + testSettings + " not found";
            testSettingsFile= findSameFileUpwards(testSettings);
        }

        if(testSettingsFile == null || !testSettingsFile.exists()) {
            LOG.error(msg);
            throw new SonarException(msg);
        }
        return testSettingsFile;
    }
    
    public String getDefault(File folder) {
        File file= getFileMatchingPattern(folder,configPattern);
        if(file!=null) {
            return file.getAbsolutePath();
        }
        return null;
    }

    public File findDefaultFileUpwards(File folder) {
        Pattern pattern = Pattern.compile(configRegex);
        while(folder !=null) {
            File file=getFileMatchingPattern(folder,pattern); 
            if(file !=null) {
                return file;
            }
            folder = folder.getParentFile();
        }
        return null;
    }

    private File getFileMatchingPattern(File folder,Pattern pattern) {
        File matchingFile = null;
        for(File file:folder.listFiles()) {
            String fileName = file.getName();
            Matcher matcher = pattern.matcher(fileName);
            if(matcher.find()) {
                if(matchingFile!=null) {
                    LOG.warn("found multiple test configuration files, found also " + fileName);                 
                } else {
                    LOG.info("found test configuration file, will be used " + fileName);
                    matchingFile=file;
                }
            }
        }
        return matchingFile;
    }

    public File findSameFileUpwards(String setting) {
        String fullPath=solutionDirectory.getAbsolutePath() + "\\" + setting ;
        File file = new File(fullPath);
        
        try {
            file = file.getCanonicalFile();
        } catch (IOException e) {
            String msg="IOException on " + file.getAbsolutePath();
            LOG.error(msg);
            throw new SonarException(msg,e);
        } 
        File folder=file.getParentFile();
        String name=file.getName();
        while(folder !=null) {
            String fullName = folder.getAbsolutePath() + "\\" + name;
            file = new File(fullName);
            if(file.exists()){
                return file ;
            }
            folder = folder.getParentFile();
        }
        return null;
    }
 
 
}
