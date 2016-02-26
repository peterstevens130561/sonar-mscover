package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class ParserThread extends Thread{

    private CoverageFileParser parser;

    public ParserThread(CoverageFileParser parser,String name) {
        super(parser,name);
        this.parser = parser;
    }
    
    public SonarCoverage getSonarCoverage() {
        return parser.getCoverage();
    }
}
