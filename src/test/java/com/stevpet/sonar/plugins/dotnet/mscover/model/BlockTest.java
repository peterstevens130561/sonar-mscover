package com.stevpet.sonar.plugins.dotnet.mscover.model;

import org.junit.Assert;
import org.junit.Test;

public class BlockTest {
    private BlockModel block = new BlockModel();
    @Test 
    public void Covered_ShouldGetSame() {
        block.setCovered(10);
        int covered = block.getCovered();
        Assert.assertEquals(10, covered);
    }
    
    @Test
    public void NotCovered_ShouldGetSame() {
        block.setNotCovered("10");
        int notCovered = block.getNotCovered();
        Assert.assertEquals(10, notCovered);
    }
    
}
