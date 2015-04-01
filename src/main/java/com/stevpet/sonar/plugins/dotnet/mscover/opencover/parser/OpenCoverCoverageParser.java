package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;

/**
 * Parses an opencover created coverage file
 */
public class OpenCoverCoverageParser implements CoverageParser {  
    public OpenCoverCoverageParser() {
    }

    @Override
    public void parse(SonarCoverage registry,File file) {
        XmlParserSubject parser = new OpenCoverParserSubject();
        OpenCoverObserver [] observers = { 
                new OpenCoverSourceFileNamesObserver(),
                new OpenCoverSequencePointsObserver(),
                new OpenCoverMissingPdbObserver()};
        for(OpenCoverObserver observer: observers) {   
            observer.setRegistry(registry);
            parser.registerObserver(observer);
        }
        parser.parseFile(file);
    }
}
