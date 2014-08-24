package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;

public class CoverageSourceFileNamesToCoverageObserver extends BaseParserObserver {
    private CoverageRegistry registry;
    private int fileId;
    public CoverageSourceFileNamesToCoverageObserver()  {
        setPattern("SourceFileNames/(SourceFileID|SourceFileName)");
    }
    
    public void setRegistry(CoverageRegistry registry) {
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
}
