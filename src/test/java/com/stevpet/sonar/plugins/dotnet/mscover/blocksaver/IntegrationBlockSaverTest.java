package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
public class IntegrationBlockSaverTest {
    
    private SensorContext context;
    private FileBlocks fileBlocks;
    private Resource<?> sonarFile;

    @Before
    public void before() {
        context = mock(SensorContext.class);
        fileBlocks = mock(FileBlocks.class);
        sonarFile = mock(Resource.class);
        
    }
    @Test
    public void Create_ShouldWork() {
        BlockSaver blockSaver = createBlockSaver();
        Assert.assertNotNull(blockSaver);
    }
    
    @Test
    public void saveMeasures_ShouldSaveThree() {
        BaseBlockSaver blockSaver = createBlockSaver();
        BlockModel summaryBlock = new BlockModel();
        when(fileBlocks.getSummaryBlock()).thenReturn(summaryBlock);
        blockSaver.saveSummaryMeasures(context, fileBlocks, sonarFile);
        verify(context,times(1)).saveMeasure(any(Resource.class),eq(CoreMetrics.IT_UNCOVERED_CONDITIONS),any(Double.class));
        verify(context,times(1)).saveMeasure(any(Resource.class),eq(CoreMetrics.IT_CONDITIONS_TO_COVER),any(Double.class));
        verify(context,times(1)).saveMeasure(any(Resource.class),eq(CoreMetrics.IT_BRANCH_COVERAGE),any(Double.class));
    }
    
    @Test
    public void saveLineMeasures_ShouldSaveTwo() {
        BaseBlockSaver blockSaver = createBlockSaver();
        when(fileBlocks.getBlocks()).thenReturn(new ArrayList<BlockModel>());
        blockSaver.saveLineMeasures(context, fileBlocks, sonarFile);
        verify(context,times(2)).saveMeasure(any(Resource.class),any(Measure.class));
    }
    
    @Test
    public void saveLineMeasuresOneBlock_ShouldSaveTwo() {
        BaseBlockSaver blockSaver = createBlockSaver();
        List<BlockModel> blocks = new ArrayList<BlockModel>() ;
        BlockModel block = new BlockModel();
        block.setNotCovered(10);
        block.setCovered(0);
        blocks.add(block);
        when(fileBlocks.getBlocks()).thenReturn(blocks);
        blockSaver.saveLineMeasures(context, fileBlocks, sonarFile);
        verify(context,times(2)).saveMeasure(any(Resource.class),any(Measure.class));
    }
    private IntegrationTestBlockSaver createBlockSaver() {
        return new IntegrationTestBlockSaver(null, null,null);
    }
}
