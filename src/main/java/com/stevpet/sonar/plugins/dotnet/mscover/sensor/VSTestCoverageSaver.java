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
package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.google.common.base.Stopwatch;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SolutionLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.ConcreteVsTestParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.LineMeasureSaver;


public class VSTestCoverageSaver implements CoverageSaver {

    static final Logger LOG = LoggerFactory
            .getLogger(VSTestCoverageSaver.class);
    private LineMeasureSaver lineSaver;
    private Project project;
    private FileSystem fileSystem;
    private List<String> artifactNames;
    /**
     * initial instantiation of the helper. Do not forget to invoke
     * setLineSaver 
     * 
     * After this you can invoke it multiple times via analyse
     * 
     * 
     * Note: the constructor takes the items that can be added through IOC, the others are in the
     * plugin
     * 
     * @param propertiesHelper
     * @param microsoftWindowsEnvironment
     * @param timeMachine
     */
    VSTestCoverageSaver(
            FileSystem fileSystem) {
        this.fileSystem=fileSystem;
    }


    public static VSTestCoverageSaver create(
           FileSystem fileSystem) {
        return new VSTestCoverageSaver(fileSystem);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageSaver#analyse
     */
    
    public void analyse(Project project, String path,List<String> artifactNames) {
        this.project = project;
        this.artifactNames = artifactNames;
        try {
            tryAnalyse(path);
        } catch (XMLStreamException e) {
            throw new SonarException("XmlStreamException", e);
        } catch (IOException e) {
            throw new SonarException("IOException",e);
        }
    }

    /*
     * @see com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageSaver#analyse
     */
    @Override
    public void analyse(Project project, List<File> xmlFiles,
            List<String> artifactNames) {
        this.project = project;
        this.artifactNames = artifactNames;
        try {
            tryAnalyseFiles(xmlFiles);
        } catch (XMLStreamException e) {
            throw new SonarException("XmlStreamException", e);
        } catch (IOException e) {
            throw new SonarException("IOException",e);
        }
    } 

    private void tryAnalyseFiles(List<File> coverageFiles)
            throws XMLStreamException, IOException {
        String projectDirectory = fileSystem.baseDir().getAbsolutePath();
        VsTestCoverageRegistry aggregatedSolutionCoverage=new VsTestCoverageRegistry(projectDirectory);
        for(File coverageFile:coverageFiles) { 
            VsTestCoverageRegistry currentsolutionCoverage=new VsTestCoverageRegistry(projectDirectory);
            invokeParserSubject(currentsolutionCoverage,coverageFile);
            aggregatedSolutionCoverage.merge(currentsolutionCoverage);
        }
        
        saveLineMeasures(aggregatedSolutionCoverage.getSolutionLineCoverageData());
        
    }

    private void tryAnalyse(String coveragePath)
            throws XMLStreamException, IOException {
        LOG.info("MsCoverPlugin : name=" + project.getName());
        String projectDirectory = fileSystem.baseDir().getAbsolutePath();
        LOG.info("MsCoverPlugin : directory=" + projectDirectory);


        VsTestCoverageRegistry registry=new VsTestCoverageRegistry(projectDirectory);
        File file = getCoverageFile(coveragePath);
        invokeParserSubject(registry,file);
        
        saveLineMeasures(registry.getSolutionLineCoverageData());
        
    }


    /**
     * Parse the coverage file, with loading the block coverage, sourcefilenames observers
     * @param fileBlocksRegistry - block coverage
     * @param sourceFileNamesRegistry - sourcefilenames
     * @throws XMLStreamException
     * @throws IOException 
     */    
    private void invokeParserSubject(VsTestCoverageRegistry registry,File coverageFile) throws XMLStreamException {
        VsTestParserFactory parserFactory = new ConcreteVsTestParserFactory();
        XmlParserSubject parserSubject = parserFactory.createCoverageParser(registry,artifactNames);

        Stopwatch sw = new Stopwatch();
        sw.start();
        parserSubject.parseFile(coverageFile);
        LOG.info("----------------------Parsing took {}ms -------------------",sw.elapsedMillis());
    }


    private File getCoverageFile(String path) {
        LOG.info("MsCover Current working directory :" + fileSystem.workDir());
        LOG.info("MsCover coverage file             :" + path); 
        if(StringUtils.isEmpty(path)) {
            throw new SonarException("MsCover: path to coverage file is not defined");
        }
        File file = new File(path);
        if(!file.exists()) {
            throw new SonarException("MsCover : coverage file '" + path + "' does not exist"); 
        }
        return file;
    }

    protected String getCurrentProjectDirectory() {
        return fileSystem.baseDir().getAbsolutePath();
    }

    public void saveLineMeasures(SolutionLineCoverage registry) {

        for (FileLineCoverage fileCoverage : registry.getFileCoverages()) {
            File file = fileCoverage.getFile();
            if(file !=null) {
                lineSaver.saveMeasures(fileCoverage, file);
            }
        }

    }

    public void setLineSaver(LineMeasureSaver lineSaver) {
        this.lineSaver=lineSaver; 
    }

}
