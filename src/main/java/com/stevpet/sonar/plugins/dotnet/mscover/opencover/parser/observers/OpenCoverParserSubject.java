package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.ParserSubject;


public class OpenCoverParserSubject extends ParserSubject {
   
    @Override
    public String[] getHierarchy() {
        String[] hierarchy= { "CoverageSession","Modules","Module","Files",
                "Classes","Class","Methods","Method","SequencePoints","BranchPoints"};
        return hierarchy;
    }
    
}
