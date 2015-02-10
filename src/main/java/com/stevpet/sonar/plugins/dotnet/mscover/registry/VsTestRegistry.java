package com.stevpet.sonar.plugins.dotnet.mscover.registry;

public class VsTestRegistry {
    
    private FileBlocksRegistry fileBlocksRegistry= new FileBlocksRegistry();

    private SourceFileNamesRegistry sourceFileNamesRegistry = new SourceFileNamesRegistry();
    private MethodToSourceFileIdMap methodToSourceFileIdMap = new MethodToSourceFileIdMap();
    private CoverageRegistry coverageRegistry ;

    public VsTestRegistry(String projectDirectory) {
        coverageRegistry = new FileCoverageRegistry(projectDirectory);
    }
    
    public FileBlocksRegistry getFileBlocksRegistry() {
        return fileBlocksRegistry;
    }
    
    public SourceFileNamesRegistry getSourceFileNamesRegistry() {
        return sourceFileNamesRegistry;
    }
    
    public CoverageRegistry getCoverageRegistry() {
        return coverageRegistry;
    }

    public MethodToSourceFileIdMap getMethodToSourceFileIdMap() {
        return methodToSourceFileIdMap;
    }
}
