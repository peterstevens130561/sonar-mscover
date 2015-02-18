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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.utils.SonarException;
import org.sonar.test.TestUtils;

public class VSTestResultsTests {
    @Test
    public void ReadResults_GetCoverageFile() throws IOException {
        File resultsFile=TestUtils.getResource("com/stevpet/sonar/plugins/dotnet/mscover/vstest/results/log");
        Assert.assertTrue(resultsFile.exists());
        VSTestStdOutParser vsTestResults= new VSTestStdOutParser();
        vsTestResults.setFile(resultsFile);
        String path=vsTestResults.getCoveragePath();
        Assert.assertNotNull(path);
        Assert.assertEquals("Mileage\\TestResults\\58925830-5ea0-4927-a4e0-de8a89cc09cd\\stevpet_RDSJ741TY1 2014-04-21 12_03_08.coverage",path);
    }
    
    @Test
    public void ReadResults_GetResultsFile() throws IOException {
        File resultsFile=TestUtils.getResource("com/stevpet/sonar/plugins/dotnet/mscover/vstest/results/log");
        Assert.assertTrue(resultsFile.exists());
        VSTestStdOutParser vsTestResults= new VSTestStdOutParser();
        vsTestResults.setFile(resultsFile);
        String path=vsTestResults.getTestResultsXmlPath();
        Assert.assertNotNull(path);
        Assert.assertEquals("Mileage\\TestResults\\stevpet_RDSJ741TY1 2014-04-21 12_03_37.trx",path);
    }
    
    @Test
    public void ReadOpenCoverResults_GetResultsFile() throws IOException {
        VSTestStdOutParser vsTestResults = prepareForOpenCoverLog();
        String path=vsTestResults.getTestResultsXmlPath();
        Assert.assertNotNull(path);
        Assert.assertEquals("C:\\Development\\Jewel.Release.Oahu\\JewelEarth\\Core\\ThinClient\\Common\\.sonar\\TestResults\\stevpet_RDSJ741TY1 2014-07-04 09_52_36.trx",path);
    }

    @Test
    public void ReadOpenCoverResults_NoCoverageFile() throws IOException {
        VSTestStdOutParser vsTestResults = prepareForOpenCoverLog();
        try {
            vsTestResults.getCoveragePath();
        } catch (SonarException e) {
            assertEquals("Could not find area \\nAttachments:\\r?\\n  (.*\\.coverage)",e.getMessage());
            return;
        }
        fail("expected exception");
  
    }
    
    
    private VSTestStdOutParser prepareForOpenCoverLog() {
        File resultsFile=TestUtils.getResource("com/stevpet/sonar/plugins/dotnet/mscover/vstest/results/opencoverlog");
        Assert.assertTrue(resultsFile.exists());
        VSTestStdOutParser vsTestResults= new VSTestStdOutParser();
        vsTestResults.setFile(resultsFile);
        return vsTestResults;
    }
}
