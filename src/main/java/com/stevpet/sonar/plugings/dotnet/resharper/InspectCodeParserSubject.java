package com.stevpet.sonar.plugings.dotnet.resharper;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;

public class InspectCodeParserSubject extends XmlParserSubject implements InspectCodeResultsParser  {
    
    @Override
    public String[] getHierarchy() {
        String[] hierarchy= { "Information","InspectionScope","IssueTypes","Project","Issues"};
        return hierarchy;
    }

    @Override
    public void parse(File file) {
        super.parseFile(file);
    }
}
    
