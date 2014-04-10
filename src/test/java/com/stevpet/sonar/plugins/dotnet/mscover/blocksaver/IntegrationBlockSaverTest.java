package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import org.junit.Test;
import org.junit.Assert;

public class IntegrationBlockSaverTest {
    @Test
    public void Create_ShouldWork() {
        BaseBlockSaver blockSaver = new IntegrationTestBlockSaver(null, null);
        Assert.assertNotNull(blockSaver);
    }
}
