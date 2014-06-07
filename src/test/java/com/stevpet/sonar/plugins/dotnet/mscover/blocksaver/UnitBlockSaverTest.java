package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.anyDouble;
public class UnitBlockSaverTest {
    
    private FileBlocks fileBlocks;
    private File sonarFile;
    private MeasureSaver measureSaver;

    @Before
    public void before() {
        fileBlocks = mock(FileBlocks.class);
        sonarFile = mock(File.class);
        measureSaver = mock(SonarMeasureSaver.class);
        
    }
    @Test
    public void Create_ShouldWork() {
        BlockMeasureSaver blockSaver = createTestBlockSaver();
        Assert.assertNotNull(blockSaver);
    }
    
    @Test
    public void saveMeasures_ShouldSaveThreeSummaryMeasures() {
        BlockMeasureSaver blockSaver = createTestBlockSaver();
        BlockModel summaryBlock = new BlockModel();
        when(fileBlocks.getSummaryBlock()).thenReturn(summaryBlock);
        blockSaver.saveMeasures(fileBlocks, sonarFile);
        verify(measureSaver,times(1)).saveMeasure(eq(CoreMetrics.UNCOVERED_CONDITIONS),anyDouble());
        verify(measureSaver,times(1)).saveMeasure(eq(CoreMetrics.CONDITIONS_TO_COVER),anyDouble());
        verify(measureSaver,times(1)).saveMeasure(eq(CoreMetrics.BRANCH_COVERAGE),anyDouble());
    }
    

    
    @Test
    public void saveLineMeasuresOneBlock_ShouldSaveTwoLineMeasures() {
        BlockMeasureSaver blockSaver = createTestBlockSaver();
        List<BlockModel> blocks = new ArrayList<BlockModel>() ;
        BlockModel block = new BlockModel();
        block.setNotCovered(10);
        block.setCovered(0);
        blocks.add(block);
        BlockModel summaryBlock = new BlockModel();
        when(fileBlocks.getSummaryBlock()).thenReturn(summaryBlock);
        when(fileBlocks.getBlocks()).thenReturn(blocks);
        blockSaver.saveMeasures(fileBlocks, sonarFile);
        verify(measureSaver,times(2)).saveMeasure(any(Measure.class));
    }
    
    private UnitTestBlockSaver createTestBlockSaver() {
        return UnitTestBlockSaver.create(measureSaver);
    }
}

