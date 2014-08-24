package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverFileNamesAndIdObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMethodObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;

public class ConcreteOpenCoverParserFactory implements OpenCoverParserFactory {
    /**
     * Creates the complete parser, with the observers registered
     * @param registry initialized registry
     */
    public XmlParserSubject createOpenCoverParser(SonarCoverage registry) {
        XmlParserSubject parser = new OpenCoverParserSubject();
        OpenCoverObserver [] observers = { 
                new OpenCoverSourceFileNamesObserver(),
                new OpenCoverSequencePointsObserver(),
                new OpenCoverMissingPdbObserver()};
        for(OpenCoverObserver observer: observers) {
            observer.setRegistry(registry);
            parser.registerObserver(observer);
        }
        return parser;
    }

    public XmlParserSubject createOpenCoverFileNamesParser(
            MethodToSourceFileIdMap map,
            SourceFileNamesRegistry sourceFileNamesRegistry) {
        XmlParserSubject parserSubject = new OpenCoverParserSubject();

        OpenCoverMethodObserver methodObserver = new OpenCoverMethodObserver();
        methodObserver.setRegistry(map);
        parserSubject.registerObserver(methodObserver);

        OpenCoverFileNamesAndIdObserver sourceFileNamesObserver = new OpenCoverFileNamesAndIdObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
        parserSubject.registerObserver(sourceFileNamesObserver);
        return parserSubject;
    }
}