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
package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.test.TestUtils;

import static org.mockito.MockitoAnnotations.initMocks;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.common.commandexecutor.CommandExecutors;
import com.stevpet.sonar.plugins.common.commandexecutor.ProcessLock;
import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.FilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestFilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.CodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.BinaryCoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.VsTestCoverageToXmlConverterBase;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class IntegrationTestSerializationTests {

    private ProjectCoverageRepository coverageData;
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock private MsCoverConfiguration msCoverProperties;
    private FilteringCoverageParser coverageParser;
    private BinaryCoverageToXmlConverter coverageToXmlConverter;
    private CoverageReader coverageReader;
    @Mock private FileSystem fileSystem;
    private CodeCoverageCommand codeCoverageCommand;
    private CommandLineExecutor commandLineExecutor;
    private Serializer serializer;
    private File workDir;
    private File testDir;
    private ProjectCoverageRepository deserialized;
    @Mock private ProcessLock processLock;
    @Mock private IntegrationTestsConfiguration integrationTestConfiguration;


    @Before
    public void before() {
        initMocks(this);
        coverageParser= new VsTestFilteringCoverageParser();
        commandLineExecutor = new WindowsCommandLineExecutor(new CommandExecutors());
        codeCoverageCommand = new WindowsCodeCoverageCommand();
        coverageToXmlConverter = new VsTestCoverageToXmlConverterBase(fileSystem, codeCoverageCommand, commandLineExecutor,processLock);
        serializer = new Serializer();
    }
    
    @Test
    @Ignore
    public void basicTest() throws IOException {
        givenCoverageFilesInDirectoryRead();
        whenSerializingTheData();
        thenDeserializedDataEquals(coverageData);
    }

    @Test
    @Ignore
    public void otherData() throws IOException {
        givenCoverageFilesInDirectoryRead();
        whenSerializingTheData();
        coverageData.linkFileNameToFileId("bogus", "4");
        thenDeserializedDataDiffers(coverageData);
    }
    
    @Test
    @Ignore
    public void otherBranchPoint() throws IOException {
        givenCoverageFilesInDirectoryRead();
        whenSerializingTheData();
        coverageData.getCoverageOfFile("1").addBranchPoint(300, true);
        thenDeserializedDataDiffers(coverageData);
    }
    
    @Test
    @Ignore
    public void otherLinePoint() throws IOException {
        givenCoverageFilesInDirectoryRead();
        whenSerializingTheData();
        coverageData.getCoverageOfFile("1").addLinePoint(300, true);
        thenDeserializedDataDiffers(coverageData);
    }
    private void thenDeserializedDataEquals(ProjectCoverageRepository coverageData) {
        File serializationFile=new File(workDir,"serialization.ser");
        deserialized = serializer.deserialize(serializationFile);
        assertEquals("serialized and deserialized should have same state",coverageData,deserialized);
        
    }

    private void thenDeserializedDataDiffers(ProjectCoverageRepository coverageData) {
        File serializationFile=new File(workDir,"serialization.ser");
        deserialized = serializer.deserialize(serializationFile);
        assertNotEquals("serialized and deserialized should have different state",coverageData,deserialized);
        
    }
    private void whenSerializingTheData() throws IOException {
        File serializationFile=new File(workDir,"serialization.ser");
        if(serializationFile.exists()) {
            FileUtils.forceDelete(serializationFile);
        }
        serializer.serialize(serializationFile, coverageData);
        assertTrue("serialization file should exist",serializationFile.exists());
        
    }

    private void whenSerializingTheDataInto() {
        File serializationFile=new File(workDir,"serialization.ser");
        deserialized = serializer.deserialize(serializationFile);
        
    }

    private void givenCoverageFilesInDirectoryRead() {
        File testFile=TestUtils.getResource("IntegrationTestSerializationTests/coveragedata/coverage.xml");
        File dummysln=TestUtils.getResource("IntegrationTestSerializationTests/solution/dummy.sln");
        File solutionDir=dummysln.getParentFile();

        workDir = new File(solutionDir,".sonar");
        try {
            FileUtils.forceMkdir(workDir);
        } catch (IOException e) {
            e.printStackTrace();
            fail("could not create directory");
        }
        assertTrue(".sonar dir should exist",workDir.exists());
        when(fileSystem.workDir()).thenReturn(workDir);
        assertNotNull("could not find test resource",testFile);
        testDir = testFile.getParentFile();
        assertNotNull("could not get parent",testDir);
        coverageData=new DefaultProjectCoverageRepository();
        coverageReader=new IntegrationTestCoverageReaderBase(microsoftWindowsEnvironment, coverageParser, integrationTestConfiguration);
        coverageReader.read(coverageData,testDir);
        assertEquals("expect some files to be read",2,coverageData.getValues().size());
    }
}
