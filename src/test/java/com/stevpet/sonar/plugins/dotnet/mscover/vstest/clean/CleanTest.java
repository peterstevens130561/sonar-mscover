/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.clean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.exceptions.MsCoverInvalidSonarWorkingDir;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.TestResultsCleaner;

public class CleanTest {
    private static final String CLEAN_TEST_EMPTY_PLACE_HOLDER_TXT = "CleanTest\\Empty\\PlaceHolder.txt";
    private static final String CLEAN_TEST_WITHFILES_PLACE_HOLDER_TXT = "CleanTest\\WithFiles\\PlaceHolder.txt";
    private FileSystemMock fileSystemMock = new FileSystemMock();
    private TestResultsCleaner runner ;
    @Before
    public void before() {
        runner=givenANewCleaner();
        
    }
    @Test(expected=IllegalStateException.class)
    public void PathNotSet_RaiseException() {
        runner.execute();
    }
    
    @Test(expected=MsCoverInvalidSonarWorkingDir.class)
    public void InvalidDir_RaiseException() {
        File testDir=TestUtils.getResource(CLEAN_TEST_EMPTY_PLACE_HOLDER_TXT);
        fileSystemMock.givenWorkDir(testDir);
        runner.execute();
    }
    
    @Test
    public void CorrectDirNoFiles_Ignore() throws IOException {
        File placeHolder=TestUtils.getResource(CLEAN_TEST_EMPTY_PLACE_HOLDER_TXT);
        File testDir=new File(placeHolder.getParentFile(),".sonar");
        FileUtils.forceMkdir(testDir);
        fileSystemMock.givenWorkDir(testDir);
        runner.execute();
        assertTrue(testDir.exists());
    }
    
    @Test
    public void CorrectDirWithFiles_ExpectDeleted() throws IOException {
        File placeHolder=TestUtils.getResource(CLEAN_TEST_WITHFILES_PLACE_HOLDER_TXT);
        File testDir=new File(placeHolder.getParentFile(),".sonar");
        File testResultsDir=new File(testDir,"TestResults");
        createFile(testDir,"test1");
        FileUtils.forceMkdir(testResultsDir);
        createFile(testResultsDir,"test1");
        createFile(testResultsDir,"test2");
        expectFilesInDir(testDir,3);
        expectFilesInDir(testResultsDir,2);
        
        fileSystemMock.givenWorkDir(testDir);
        runner.execute();
        assertTrue(testDir.exists());
        expectFilesInDir(testDir,1);
        assertFalse(testResultsDir.exists());
    }

    private TestResultsCleaner givenANewCleaner() {
        return new TestResultsCleaner(fileSystemMock.getMock());
    }

    private void expectFilesInDir(File testDir, int count) {
        Collection<File> files=FileUtils.listFiles(testDir, TrueFileFilter.INSTANCE,TrueFileFilter.INSTANCE);
        assertEquals(count,files.size());
    }
    
    private void createFile(File parentFile,String name) throws IOException {
        File newFile = new File(parentFile, name) ;
        FileUtils.write(newFile, "bogus");
    }
}
