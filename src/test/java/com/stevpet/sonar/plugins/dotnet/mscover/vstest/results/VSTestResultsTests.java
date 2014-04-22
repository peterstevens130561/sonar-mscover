package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.test.TestUtils;

public class VSTestResultsTests {
    @Test
    public void ReadResults_GetCoverageFile() throws IOException {
        File resultsFile=TestUtils.getResource("com/stevpet/sonar/plugins/dotnet/mscover/vstest/results/log");
        Assert.assertTrue(resultsFile.exists());
        VSTestResults vsTestResults= new VSTestResults();
        vsTestResults.setFile(resultsFile);
        String path=vsTestResults.getCoveragePath();
        Assert.assertNotNull(path);
        Assert.assertEquals("Mileage\\TestResults\\58925830-5ea0-4927-a4e0-de8a89cc09cd\\stevpet_RDSJ741TY1 2014-04-21 12_03_08.coverage",path);
    }
    
    @Test
    public void ReadResults_GetResultsFile() throws IOException {
        File resultsFile=TestUtils.getResource("com/stevpet/sonar/plugins/dotnet/mscover/vstest/results/log");
        Assert.assertTrue(resultsFile.exists());
        VSTestResults vsTestResults= new VSTestResults();
        vsTestResults.setFile(resultsFile);
        String path=vsTestResults.getResultsPath();
        Assert.assertNotNull(path);
        Assert.assertEquals("Mileage\\TestResults\\stevpet_RDSJ741TY1 2014-04-21 12_03_37.trx",path);
    }
}
