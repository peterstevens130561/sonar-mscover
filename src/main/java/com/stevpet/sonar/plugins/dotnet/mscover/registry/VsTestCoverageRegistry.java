package com.stevpet.sonar.plugins.dotnet.mscover.registry;


import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNamesModel;

public class VsTestCoverageRegistry {
    
    private SourceFileNamesRegistry sourceFileNamesRegistry = new SourceFileNamesRegistry();
    private MethodToSourceFileIdMap methodToSourceFileIdMap = new MethodToSourceFileIdMap();
    private SolutionLineCoverage solutionLineCoverage ;

    public VsTestCoverageRegistry(String projectDirectory) {
        solutionLineCoverage = new DefaultSolutionLineCoverage(projectDirectory);
    }
    
    
    public SourceFileNamesRegistry getSourceFileNamesRegistry() {
        return sourceFileNamesRegistry;
    }
    
    public SolutionLineCoverage getCoverageRegistry() {
        return solutionLineCoverage;
    }

    public MethodToSourceFileIdMap getMethodToSourceFileIdMap() {
        return methodToSourceFileIdMap;
    }
    
    public void merge(VsTestCoverageRegistry registryToMerge) {
        Collection<SourceFileNamesModel> mappings = sourceFileNamesRegistry.values();
        for(SourceFileNamesModel mapping: mappings) {
             int toMergeId=mapping.getSourceFileID();
            String toMergeName=mapping.getSourceFileName();
            int destinationId=sourceFileNamesRegistry.getSourceFileId(toMergeName);
            solutionLineCoverage.merge(destinationId,toMergeId,registryToMerge.getCoverageRegistry());
        }
    }
}
