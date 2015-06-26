package com.stevpet.sonar.plugings.dotnet.resharper;

import java.io.File;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;

public class InspectCodeParserSubject extends XmlParserSubject  {
    
    @Override
    public String[] getHierarchy() {
        String[] hierarchy= { "Information","InspectionScope","IssueTypes","Project","Issues"};
        return hierarchy;
    }

}
    
