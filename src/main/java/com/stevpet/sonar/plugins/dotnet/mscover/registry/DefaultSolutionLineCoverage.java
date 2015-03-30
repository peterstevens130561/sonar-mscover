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
package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;

public class DefaultSolutionLineCoverage implements SolutionLineCoverage {
    private static final Logger LOG = LoggerFactory
            .getLogger(DefaultSolutionLineCoverage.class);

    private Map<Integer, FileLineCoverage> registry = new HashMap<Integer, FileLineCoverage>();

    private int lineCount;
    SourceFilePathHelper pathHelper;
 

    private int coveredLineCount;

    public DefaultSolutionLineCoverage(String projectDirectory) {
        pathHelper = new SourceFilePathHelper();
        pathHelper.setProjectPath(projectDirectory);
    }

    /**
     * add a point to the file coverage
     * 
     * @param fileId
     *            of file
     * @param point
     *            unique! point
     */
    public void addCoveredFileLine(int fileId, CoveragePoint point) {
        FileLineCoverage fileCoverage = getFileCoverage(fileId);
        fileCoverage.addCoveredLine(point);
        lineCount++;
        coveredLineCount++;
    }

    public void addUnCoveredFileLine(Integer sourceFileID, CoveragePoint point) {
        FileLineCoverage fileCoverage = getFileCoverage(sourceFileID);
        fileCoverage.addUnCoveredLine(point);
        lineCount++;  
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.CoverageRegistry#addFile(int,
     * java.io.File)
     */
    public void addFile(int fileId, String fullPath)  {
        FileLineCoverage fileCoverage = getFileCoverage(fileId);
        pathHelper.setFilePath(fullPath);
        if (pathHelper.isModuleInSolution()) {
            String projectPath = pathHelper.getSolutionPath();
            File file = new File(projectPath);
            LOG.info("MsCover path : "+ projectPath);
            fileCoverage.setCanonicalFile(file);
            if (!file.exists()) {
                LOG.warn("Ignoring that file '" + projectPath
                        + "' does not exist. Original :" + fullPath);
            }

        }
    }


    @Override
    public void merge(int destinationId,int sourceId,
            SolutionLineCoverage sourceSolutionLineCoverage) {
        FileLineCoverage baselineFileLineCoverage = registry.get(destinationId);
        FileLineCoverage sourceFileLineCoverage = sourceSolutionLineCoverage.getFileLineCoverage(sourceId);
        if(baselineFileLineCoverage == null) {
            baselineFileLineCoverage=new FileLineCoverage(destinationId,sourceFileLineCoverage);
            registry.put(destinationId, baselineFileLineCoverage);  
        } else {
            baselineFileLineCoverage.merge(sourceFileLineCoverage);
        }
        
    }
    /**
     * get the filecoverage
     * 
     * @param fileId
     *            of file
     * @return
     */
    private FileLineCoverage getFileCoverage(int fileId) {
        if (!registry.containsKey(fileId)) {
            registry.put(fileId, new FileLineCoverage(fileId));
        }
        return registry.get(fileId);
    }

    
    /**
     * Returns all FileCoverage in the registry. Note that sourcefiles that are not in the project will have their File set to null
     * @return
     */
    public Collection<FileLineCoverage> getFileCoverages() {
        return registry.values();
    }



    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.CoverageRegistry#getFileCount()
     */
    public int getFileCount() {
        return registry.size();
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getCoveredLineCount() {
        return coveredLineCount;
    }
    
    protected Map<Integer, FileLineCoverage> getRegistry() {
        return registry ;
    }

    @Override
    public FileLineCoverage getFileLineCoverage(int sourceId) {
        return registry.get(sourceId);
    }




}