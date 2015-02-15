package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SolutionLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;

public class VsTestSourceFileNamesToCoverageObserver extends VsTestCoverageObserver {
    SolutionLineCoverage registry;
    private int fileId;
    public VsTestSourceFileNamesToCoverageObserver()  {
        setPattern("SourceFileNames/(SourceFileID|SourceFileName)");
    }
    
    public void setRegistry(SolutionLineCoverage registry) {
        this.registry = registry;
    }
    
    @ElementMatcher(elementName="SourceFileID")
    public void sourceFileIDMatcher(String value) {
        fileId=Integer.parseInt(value);
    }
    
    @ElementMatcher(elementName="SourceFileName")
    public void sourceFileNameMatcher(String sourceFileName){
        registry.addFile(fileId, sourceFileName);  
    }

    @Override
    public void setVsTestRegistry(VsTestCoverageRegistry vsTestRegistry) {
        this.registry =vsTestRegistry.getCoverageRegistry();
    }
}
