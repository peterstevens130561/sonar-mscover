package com.stevpet.sonar.plugings.dotnet.resharper.issuesparser;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;

public class InspectCodeParserSubject extends XmlParserSubject  {
    
    @Override
    public String[] getHierarchy() {
        String[] hierarchy= { "Information","InspectionScope","IssueTypes","Project","Issues"};
        return hierarchy;
    }

}
    
