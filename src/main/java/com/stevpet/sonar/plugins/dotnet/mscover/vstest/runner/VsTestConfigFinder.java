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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration;


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
            msg=DefaultMsCoverConfiguration.MSCOVER_TESTSETTINGS + " not set, and no testsettings file found";
            testSettingsFile= findDefaultFileUpwards(solutionDirectory);
            if(testSettingsFile==null) {
                LOG.error(msg);
                throw new IllegalStateException(msg);
            }
            return testSettingsFile;
        }
        
        testSettingsFile=findAbsoluteFile(testSettings);
        if(testSettingsFile!=null) {
            return testSettingsFile;
        }
        

        testSettingsFile= findSameFileUpwards(solutionDirectory,testSettings);

        if(testSettingsFile == null || !testSettingsFile.exists()) {
            msg=DefaultMsCoverConfiguration.MSCOVER_TESTSETTINGS + "=" + testSettings + " not found";
            LOG.error(msg);
            throw new IllegalStateException(msg);
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

    private File findAbsoluteFile(String setting) {
        File absoluteFile = new File(setting);
        if(absoluteFile.isAbsolute()) {
            if(!absoluteFile.exists()) {
                throw new IllegalStateException("absolute test runner setting does not exist" + setting);
            }
            return absoluteFile;
        }
        
        return null;
    }
    private File findSameFileUpwards(File solutionDirectory,String setting) {
        File folder=solutionDirectory.getAbsoluteFile();
        while(folder !=null) {
            File file=new File(folder,setting);
            File canonicalFile = getCanonicalFile(file);
            if(canonicalFile.exists()){
                return canonicalFile;
            }
            folder = folder.getParentFile();
        }
        return null;
    }

    /**
     * get the canonicalFile (so any ../ is removed)
     * @param file
     * @return the canonical file
     */
    private File getCanonicalFile(File file) {
        File canonicalFile;
        try {
            canonicalFile = file.getCanonicalFile();
        } catch (IOException e) {
            throw new IllegalStateException("IOException on " + file.getAbsolutePath(),e);
        }
        return canonicalFile;
    }

}
