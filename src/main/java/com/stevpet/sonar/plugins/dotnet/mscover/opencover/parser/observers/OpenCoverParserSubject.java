package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;


public class OpenCoverParserSubject extends XmlParserSubject {
   
    @Override
    public String[] getHierarchy() {
        String[] hierarchy= { "CoverageSession","Modules","Module","Files",
                "Classes","Class","Methods","Method","SequencePoints","BranchPoints"};
        return hierarchy;
    }
    
}
