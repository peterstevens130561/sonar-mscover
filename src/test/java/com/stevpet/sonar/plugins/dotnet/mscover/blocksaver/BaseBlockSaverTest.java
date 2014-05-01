package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNamesModel;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;

public class BaseBlockSaverTest {

    private SensorContext context;
    private Project project;
    @Before
    public void before() {
        
    }
    @Test
    public void CreateSaver_WithNullParms_ShouldCreate() {
        BaseBlockSaver saver = new DummyBaseBlockSaver(null,null);
    }
    
    
    @Test
    public void FileNotInSolution_ShouldBeSkipped() throws IOException {
        FileBlocksRegistry fileBlocksRegistry = new FileBlocksRegistry();
        BlockModel model = new BlockModel();
        
        fileBlocksRegistry.add("1", model);
        
        SourceFileNamesRegistry sourceFileNamesRegistry = new SourceFileNamesRegistry() ;
        SourceFileNamesModel sourceFileNamesModel = new SourceFileNamesModel();
        sourceFileNamesModel.setSourceFileID("1");
        sourceFileNamesModel.setSourceFileName("a/b/c");
        
        sourceFileNamesRegistry.add("1", sourceFileNamesModel);     
        SourceFilePathHelper sourceFilePathHelper = new SourceFilePathHelper();
        sourceFilePathHelper.setProjectPath("d");
        BaseBlockSaver saver = new DummyBaseBlockSaver(null,null);
        saver.setFileBlocksRegistry(fileBlocksRegistry);
        saver.setSourceFileNamesRegistry(sourceFileNamesRegistry);
        saver.setSourceFilePathHelper(sourceFilePathHelper);
        saver.save();
    }
    
    @Test
    public void FileInSolution_ShouldBeAccepted() throws IOException {
        FileBlocksRegistry fileBlocksRegistry = new FileBlocksRegistry();
        BlockModel model = new BlockModel();
        
        fileBlocksRegistry.add("1", model);
        
        SourceFileNamesRegistry sourceFileNamesRegistry = new SourceFileNamesRegistry() ;
        SourceFileNamesModel sourceFileNamesModel = new SourceFileNamesModel();
        sourceFileNamesModel.setSourceFileID("1");
        sourceFileNamesModel.setSourceFileName("a/b/c");
        
        sourceFileNamesRegistry.add("1", sourceFileNamesModel);     
        SourceFilePathHelper sourceFilePathHelper = new SourceFilePathHelper();
        sourceFilePathHelper.setProjectPath("a");
        BaseBlockSaver saver = new DummyBaseBlockSaver(null,null);
        saver.setFileBlocksRegistry(fileBlocksRegistry);
        saver.setSourceFileNamesRegistry(sourceFileNamesRegistry);
        saver.setSourceFilePathHelper(sourceFilePathHelper);
        saver.save();
    }
    
    
    private class DummyBaseBlockSaver extends BaseBlockSaver {

        public DummyBaseBlockSaver(SensorContext context, Project project) {
            super(context, project);

        }

        @Override
        public void saveSummaryMeasures(SensorContext context,
                FileBlocks fileBlocks, Resource<?> sonarFile) {
            
        }

        @Override
        public void saveLineMeasures(SensorContext context,
                FileBlocks fileBlocks, Resource<?> sonarFile) {
            
        }
    

    }
}
