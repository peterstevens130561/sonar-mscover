/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
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

    
    public VsTestConfigFinder() {

    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.TestConfigFinder#getTestSettingsFileOrDie(java.lang.String)
     */
    public File getTestSettingsFileOrDie(File solutionDirectory,String testSettings) {
        String msg;
        File testSettingsFile;
        if(StringUtils.isEmpty(testSettings)) {
            msg=PropertiesHelper.MSCOVER_TESTSETTINGS + " not set, and no testsettings file found";
            testSettingsFile= findDefaultFileUpwards(solutionDirectory);

        } else {
            msg=PropertiesHelper.MSCOVER_TESTSETTINGS + "=" + testSettings + " not found";
            testSettingsFile= findSameFileUpwards(solutionDirectory,testSettings);
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

    private File findSameFileUpwards(File solutionDirectory,String setting) {
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
