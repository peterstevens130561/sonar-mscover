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
package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.model.TestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry.ForEachUnitTestFile;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.NullResource;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class TrxTestSaver implements TestSaver {

    static final Logger LOG = LoggerFactory
            .getLogger(TrxTestSaver.class);
    
    private TestResultsSaver testResultsSaver  ;
    private SourceFileNameTable sourceFileNamesRegistry ;
    private UnitTestFilesResultRegistry unitTestFilesResultRegistry;
    private SourceFilePathHelper sourceFilePathHelper;
    TestResults  projectSummaryResults;
    MeasureSaver measureSaver;
    private ResourceMediator resourceMediator;

    private SensorContext sensorContext;

    private Project project;
    

    public TrxTestSaver(SensorContext sensorContext, Project project,ResourceMediator resourceMediator,MeasureSaver measureSaver) {
        this.resourceMediator = resourceMediator;
        this.measureSaver = measureSaver;
        this.sensorContext = sensorContext;
        this.project=project;
        testResultsSaver = new TestResultsSaver(measureSaver);
    }
    public SourceFileNameTable getSourceFileNamesRegistry() {
        return sourceFileNamesRegistry;
    }

    public void setSourceFileNamesRegistry(
            SourceFileNameTable sourceFileNamesRegistry) {
        this.sourceFileNamesRegistry = sourceFileNamesRegistry;
    }

    public UnitTestFilesResultRegistry getUnitTestFilesResultRegistry() {
        return unitTestFilesResultRegistry;
    }

    public void setUnitTestFilesResultRegistry(
            UnitTestFilesResultRegistry unitTestFilesResultRegistry) {
        this.unitTestFilesResultRegistry = unitTestFilesResultRegistry;
    }


    public void setSourceFilePathHelper(SourceFilePathHelper sourceFilePathHelper) {
        this.sourceFilePathHelper = sourceFilePathHelper;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestSaver#save()
     */
    public void save() {
        projectSummaryResults = new TestResults();
        unitTestFilesResultRegistry.forEachUnitTestFile(new SaveUnitTestFileMeasures());

    }

    class SaveUnitTestFileMeasures implements ForEachUnitTestFile {

        public void execute(String fileID, ClassUnitTestResult fileResults) {
        ResourceSeam sonarFile = tryToGetUnitTestResource(fileID);
        if(!(sonarFile instanceof NullResource)) {
            projectSummaryResults.add(fileResults);
        }
        testResultsSaver.saveSummaryMeasures(fileResults, sonarFile);
        testResultsSaver.saveTestCaseMeasures(fileResults, sonarFile);

    }

        private ResourceSeam tryToGetUnitTestResource(
                String fileID) {
            String sourceFileName=sourceFileNamesRegistry.getSourceFileName(fileID);

            File sourceFile = sourceFilePathHelper.getCanonicalFile(sourceFileName);
            if(sourceFile == null) {
                LOG.warn("Could not get unit test file for file "+sourceFileName);
                return new NullResource();
            }
            return resourceMediator.getSonarResource(sensorContext, project, sourceFile);
        }
   
    }
}