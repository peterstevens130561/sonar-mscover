package com.stevpet.sonar.plugins.dotnet.mscover.blocksaver;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.BaseSaver;

public abstract class BaseBlockSaver extends BaseSaver implements BlockRegistry {

    private static final Logger LOG = LoggerFactory
            .getLogger(BaseBlockSaver.class);
    private SourceFileNamesRegistry sourceFileNamesRegistry;
    private FileBlocksRegistry fileBlocksRegistry;
    private SourceFilePathHelper sourceFilePathHelper ;
    private SensorContext context;
    private Project project;
 
    public BaseBlockSaver(SensorContext context, Project project,
            CoverageRegistry registry) {
        super();
        this.context = context;
        this.project=project;
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

    public void save() throws IOException {
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
    
    abstract void saveSummaryMeasures(SensorContext context,FileBlocks fileBlocks, Resource<?> sonarFile);
    abstract void saveLineMeasures(SensorContext context,FileBlocks fileBlocks, Resource<?> sonarFile);   
    
    public org.sonar.api.resources.File getSonarFile(String fileID) throws IOException {
        String coveragePath = sourceFileNamesRegistry.getSourceFileName(fileID);
        
        String canonicalPath = sourceFilePathHelper.getCanonicalPath(coveragePath);
        if(canonicalPath == null) {
            return null;
        }
        File file = new File(canonicalPath);
        org.sonar.api.resources.File sonarFile = org.sonar.api.resources.File
                .fromIOFile(file, project);
        if (sonarFile == null) {
            LOG.debug("Could not create sonarFile for "
                    + file.getAbsolutePath());
            return null;
        }
        if (!context.isIndexed(sonarFile, false)) {
            LOG.debug("Skipping not indexed file " + sonarFile.getLongName());
            return null;
        }
        String longName = sonarFile.getLongName();
        if(!resourceFilter.isPassed(longName)) {
            return null;
        }
        if (!dateFilter.isResourceIncludedInResults(sonarFile)) {
            LOG.debug("Skipping file of which commit date is before cutoff date " +sonarFile.getLongName());
            return null;
        }

        return sonarFile ;
    }

  


    protected double convertPercentage(Number percentage) {
        return ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }


    protected Double getCoverage(BlockModel methodBlock) {
        if(methodBlock.getBlocks()==0) {
            return 1.0;
        }
        return (double) methodBlock.getCovered() / (double) methodBlock.getBlocks();
    }

}
