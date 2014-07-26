package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
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
        String pattern ;
        String msg;
        if(StringUtils.isEmpty(testSettings)) {
            msg=PropertiesHelper.MSCOVER_TESTSETTINGS + " not set, and no testsettings file found";
            pattern=configRegex;

        } else {
            msg=PropertiesHelper.MSCOVER_TESTSETTINGS + "=" + testSettings + " not found";
            pattern=testSettings;
        }
        File testSettingsFile= findFileUpwards(solutionDirectory,pattern);
        if(testSettingsFile == null || !testSettingsFile.exists()) {
            LOG.error(msg);
            throw new SonarException(msg);
        }
        return testSettingsFile;
    }
    
    public String getDefault(File folder) {
        File file= getMatchingFile(folder,configPattern);
        if(file!=null) {
            return file.getAbsolutePath();
        }
        return null;
    }

    public File findFileUpwards(File folder, String regex) {
        boolean found=false;
        Pattern pattern = Pattern.compile(regex);
        while(folder !=null) {
            File file=getMatchingFile(folder,pattern); 
            if(file !=null) {
                return file;
            }
            folder = folder.getParentFile();
        }
        return null;
    }

    private File getMatchingFile(File folder,Pattern pattern) {
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

 
}
