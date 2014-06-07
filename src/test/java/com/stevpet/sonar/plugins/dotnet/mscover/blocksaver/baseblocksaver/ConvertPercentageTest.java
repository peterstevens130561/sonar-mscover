package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.baseblocksaver;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BaseBlockSaver;

public class ConvertPercentageTest {


    @Test
    public void SomeNumber_ShouldBe100Times() {
        double data=91.5345;
        double result = BaseBlockSaver.convertPercentage(data);
        assertEquals(data*100,result,0.001);
    }
}
