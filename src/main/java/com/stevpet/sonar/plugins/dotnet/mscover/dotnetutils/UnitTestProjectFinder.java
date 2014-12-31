/*
 * SonarQube MSCover coverage plugin
 * Copyright (C) 2014 Peter Stevens
 * peter@famstevens.eu
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
package com.stevpet.sonar.plugins.dotnet.mscover.dotnetutils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;


public class UnitTestProjectFinder {
    Logger LOG = LoggerFactory.getLogger(UnitTestProjectFinder.class);
    private File startDirectory;
    private File currentDirectory;
    private List<String> projects;
    /**
     * 
     * @param startDirectory - directory where to start. Maybe in dir where the solution is, or below
     */
    public UnitTestProjectFinder(File startDirectory) {
        this.startDirectory=startDirectory;
        this.currentDirectory=startDirectory;
    }
    /**
     * Find the solution with given name start at the startDirectory, going upwards. If not found SonarException is thrown
     * @param solutionName - name of the solution (including the .sln)
     */
    public UnitTestProjectFinder gotoDirWithSolution(String solutionName) {
        List<String> projectNames = new ArrayList<String>();
        File solutionFile;
        do {
            solutionFile=new File(currentDirectory,solutionName);
            if(solutionFile.exists()) {
                break;
            }
            currentDirectory=currentDirectory.getParentFile();

        } while(currentDirectory!=null);
        if(!solutionFile.exists()) {
            String msg="Could not find solution " + solutionName + " upwards from directory " + startDirectory;
            LOG.error(msg);
            throw new SonarException(msg);
        }
        return this;
    }
    
    /**
     * Find projects matching the pattern
     * @param pattern 
     * @return list of projects. If not projects are found the list is empty
     */
    public List<String> findProjects(String pattern) {
        projects = new ArrayList<String>();
        search(currentDirectory,pattern);
        return projects;
    }
    
    private void search(File searchDirectory,String pattern) {
        File[] files =searchDirectory.listFiles();
        for(File file:files) {
            String name=file.getName();
            if(file.isDirectory()) {
                search(file,pattern);
            } else if (name.matches(pattern)) {
                String strippedName=name.replaceAll("\\.c[xs]proj$", "");
                projects.add(strippedName);
            }
            
        }
        
    }

}