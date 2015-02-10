package com.stevpet.sonar.plugins.dotnet.mscover.registry;

public class VsTestRegistry {
    
    private SourceFileNamesRegistry sourceFileNamesRegistry = new SourceFileNamesRegistry();
    private MethodToSourceFileIdMap methodToSourceFileIdMap = new MethodToSourceFileIdMap();
    private LineCoverageRegistry coverageRegistry ;

    public VsTestRegistry(String projectDirectory) {
        coverageRegistry = new FileCoverageRegistry(projectDirectory);
    }
    
    
    public SourceFileNamesRegistry getSourceFileNamesRegistry() {
        return sourceFileNamesRegistry;
    }
    
    public LineCoverageRegistry getCoverageRegistry() {
        return coverageRegistry;
    }

    public MethodToSourceFileIdMap getMethodToSourceFileIdMap() {
        return methodToSourceFileIdMap;
    }
}
