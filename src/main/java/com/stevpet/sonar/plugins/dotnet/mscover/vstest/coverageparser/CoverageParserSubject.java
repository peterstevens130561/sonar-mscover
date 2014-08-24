package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;


public class CoverageParserSubject extends XmlParserSubject {
   
    @Override
    public String[] getHierarchy() {
        String[] hierarchy= { "Module","NamespaceTable","Class","Method","Lines","SourceFileNames"};
        return hierarchy;
    }
    
}
