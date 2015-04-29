package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;

public class VsTestSourceFileNamesObserver extends BaseParserObserver {
	private SourceFileNameTable sourceFileNameTable;
	private String sourceFileID;
	
	public void setRegistry(SourceFileNameTable sourceFileNameTable) {
		this.sourceFileNameTable = sourceFileNameTable;
		
	} 
	
    public VsTestSourceFileNamesObserver()  {
        setPattern("SourceFileNames/(SourceFileID|SourceFileName)");
    }
    
    
    @ElementMatcher(elementName="SourceFileID")
    public void sourceFileIDMatcher(String value) {
        sourceFileID=value;
    }
    
    @ElementMatcher(elementName="SourceFileName")
    public void sourceFileNameMatcher(String sourceFileName){
    	sourceFileNameTable.add(sourceFileID,sourceFileName);
    }

}