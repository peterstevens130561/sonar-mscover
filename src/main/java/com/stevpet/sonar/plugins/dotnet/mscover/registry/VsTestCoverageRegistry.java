package com.stevpet.sonar.plugins.dotnet.mscover.registry;


import java.util.Collection;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;

public class VsTestCoverageRegistry {
    
    private SourceFileNameTable sourceFileNamesTable = new SourceFileNameTable();
    private MethodToSourceFileIdMap methodToSourceFileIdMap = new MethodToSourceFileIdMap();
    private SolutionLineCoverage solutionLineCoverage ;

    public VsTestCoverageRegistry(String projectDirectory) {
        solutionLineCoverage = new DefaultSolutionLineCoverage(projectDirectory);
    }
    
    
    public SourceFileNameTable getSourceFileNameTable() {
        return sourceFileNamesTable;
    }
    
    public SolutionLineCoverage getSolutionLineCoverageData() {
        return solutionLineCoverage;
    }

    public MethodToSourceFileIdMap getMethodToSourceFileIdMap() {
        return methodToSourceFileIdMap;
    }
    
    public void merge(VsTestCoverageRegistry registryToMerge) {
        Collection<SourceFileNameRow> rows = registryToMerge.getSourceFileRows();
        for(SourceFileNameRow sourceFileRow: rows) {
            int toMergeId=sourceFileRow.getSourceFileID();
            String toMergeName=sourceFileRow.getSourceFileName();
            int destinationId=sourceFileNamesTable.getSourceFileId(toMergeName);
            solutionLineCoverage.merge(destinationId,toMergeId,registryToMerge.getSolutionLineCoverageData());
        }
    }


    private Collection<SourceFileNameRow> getSourceFileRows() {
        return sourceFileNamesTable.values();
    }
}
