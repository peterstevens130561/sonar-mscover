package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import java.io.File;


public class ParserThread extends Thread{


    public ParserThread(CoverageFileParser parser,String name) {
        super(parser,name);
    }
    
}
