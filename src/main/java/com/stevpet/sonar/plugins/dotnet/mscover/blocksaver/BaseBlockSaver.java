package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import java.io.File;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.BaseSaver;

public abstract class BaseBlockSaver extends BaseSaver implements BlockRegistry {

    private SourceFileNamesRegistry sourceFileNamesRegistry;
    private FileBlocksRegistry fileBlocksRegistry;
    private SourceFilePathHelper sourceFilePathHelper ;
    private SensorContext context;

    public BaseBlockSaver(SensorContext context, Project project) {
        super(context,project);
        this.context = context;
    }


    public void setSourceFileNamesRegistry(
            SourceFileNamesRegistry sourceFileNamesRegistry) {
        this.sourceFileNamesRegistry = sourceFileNamesRegistry;
    }

    public void setFileBlocksRegistry(FileBlocksRegistry fileBlocksRegistry) {
        this.fileBlocksRegistry = fileBlocksRegistry;
        
    }
    
    public void setSourceFilePathHelper(SourceFilePathHelper sourceFilePathHelper) {
        this.sourceFilePathHelper =sourceFilePathHelper;
    }

    public void save() {
        for (FileBlocks fileBlocks: fileBlocksRegistry.values()) {
            String fileID = fileBlocks.getFileId();
            org.sonar.api.resources.File sonarFile = getSonarFile(fileID);
            if(sonarFile==null) {
                continue;
            }

            saveSummaryMeasures(context, fileBlocks, sonarFile);
            saveLineMeasures(context, fileBlocks,sonarFile);
        }

    }
    
    public abstract void saveSummaryMeasures(SensorContext context,FileBlocks fileBlocks, Resource<?> sonarFile);
    public abstract void saveLineMeasures(SensorContext context,FileBlocks fileBlocks, Resource<?> sonarFile);   
    
    public org.sonar.api.resources.File getSonarFile(String fileID) {
        String coveragePath = sourceFileNamesRegistry.getSourceFileName(fileID);
        
        File file = sourceFilePathHelper.getCanonicalFile(coveragePath);
        if(file == null) {
            return null;
        }
        return getSonarFileResource(file);
    }

  


    protected double convertPercentage(Number percentage) {
        return ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }


    protected Double getCoverage(BlockModel methodBlock) {
        if(methodBlock.getBlocks()==0) {
            return 100.0;
        }
        return (double) (methodBlock.getCovered()*100.0) / (double) methodBlock.getBlocks();
    }

}
