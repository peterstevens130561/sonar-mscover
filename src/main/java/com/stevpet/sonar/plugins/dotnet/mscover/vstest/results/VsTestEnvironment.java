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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.ProjectSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.SonarProjectSeam;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class VsTestEnvironment implements BatchExtension {

    private ProjectSeam projectSeam = new SonarProjectSeam();
    private String coverageXmlPath;
    private String resultsXmlPath;
    private boolean testsHaveRun=false;
    private String targetDir;

    private SonarCoverage sonarCoverage = new SonarCoverage();
    public String getXmlCoveragePath() {
        return coverageXmlPath;
    }
    public void setCoverageXmlPath(String xmlCoveragePath) {
        this.coverageXmlPath = xmlCoveragePath;
    }
    public String getXmlResultsPath() {
        return resultsXmlPath;
    }
    public void setTestResultsXmlPath(String xmlResultsPath) {
        this.resultsXmlPath = xmlResultsPath;
    }
    public void setTestsHaveRun() {
        testsHaveRun=true;      
    }
    public boolean getTestsHaveRun() {
        return testsHaveRun;
    }
    
    public void setSonarCoverage(SonarCoverage sonarCoverage) {
        this.sonarCoverage = sonarCoverage;
        this.setTestsHaveRun();
    }
    
    public SonarCoverage getSonarCoverage() {
        return sonarCoverage;
    }
    public void setCoverageXmlFile(Project project, String string) {
        projectSeam.setProject(project);
        File opencoverCoverageFile= projectSeam.getSonarFile("coverage-report.xml");
        String openCoverCoveragePath= opencoverCoverageFile.getAbsolutePath();
        setCoverageXmlPath(openCoverCoveragePath); 
    }
    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }
}
