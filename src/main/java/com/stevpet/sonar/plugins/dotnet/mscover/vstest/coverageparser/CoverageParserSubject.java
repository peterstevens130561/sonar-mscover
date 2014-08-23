package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;


public class CoverageParserSubject extends ParserSubject {
   
    @Override
    public String[] getHierarchy() {
        String[] hierarchy= { "Module","NamespaceTable","Class","Method","Lines","SourceFileNames"};
        return hierarchy;
    }
    
}
