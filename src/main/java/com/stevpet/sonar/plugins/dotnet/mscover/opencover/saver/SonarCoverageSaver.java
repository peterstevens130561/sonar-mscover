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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.DeprecatedDefaultBranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.DeprecatedDefaultLineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.FileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class SonarCoverageSaver {

    private SonarCoverage sonarCoverageRegistry;
    private MeasureSaver measureSaver;
    private FileCoverageSaver sonarBranchSaver;
    private FileCoverageSaver sonarLineSaver;
    private List<File> testFiles = new ArrayList<File>();
    public SonarCoverageSaver(MeasureSaver measureSaver) {

        this.measureSaver=measureSaver;
  
    }

    public void setCoverageRegistry(SonarCoverage sonarCoverageRegistry) {
        this.sonarCoverageRegistry=sonarCoverageRegistry;
    }
    
    public void save() {
        sonarBranchSaver = new DeprecatedDefaultBranchFileCoverageSaver(measureSaver);
        sonarLineSaver = new DeprecatedDefaultLineFileCoverageSaver(measureSaver);
        for(SonarFileCoverage fileCoverage:sonarCoverageRegistry.getValues()) {
            saveFileResults(fileCoverage);
        }
    }

    private void saveFileResults(SonarFileCoverage fileCoverage) {
        File file=new File(fileCoverage.getAbsolutePath());

        if(!testFiles.contains(file)) {
            sonarLineSaver.saveMeasures(fileCoverage.getLinePoints(), file);
            sonarBranchSaver.saveMeasures(fileCoverage.getBranchPoints(), file);
        }
    }

    public void setExcludeSourceFiles(List<File> testFiles) {
        this.testFiles=testFiles;
    }

}
