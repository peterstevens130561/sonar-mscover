package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverFileNamesAndIdObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverMethodObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverMissingPdbObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.MethodBlocksObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.MethodObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.CoverageSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.ResultsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.ResultsParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.UnitTestObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.results.UnitTestResultObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;

public class ConcreteParserFactory implements ParserFactory {
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.CoverageParserFactory#createDefault(com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry, com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry)
     */
    public ParserSubject createCoverageParser(FileBlocksRegistry fileBlocksRegistry,
            SourceFileNamesRegistry sourceFileNamesRegistry) {

        ParserSubject parserSubject = new CoverageParserSubject();

        MethodBlocksObserver methodBlocksObserver = new MethodBlocksObserver();
        methodBlocksObserver.setRegistry(fileBlocksRegistry);
        parserSubject.registerObserver(methodBlocksObserver);

        CoverageSourceFileNamesObserver sourceFileNamesObserver = new CoverageSourceFileNamesObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
        parserSubject.registerObserver(sourceFileNamesObserver);
        return parserSubject;
    }

    /**
     * Used to be able to find the sourcefile by specifying the method
     * @param map
     * @param sourceFileNamesRegistry
     * @return
     */
    public ParserSubject createFileNamesParser(MethodToSourceFileIdMap map,
            SourceFileNamesRegistry sourceFileNamesRegistry) {

        ParserSubject parserSubject = new CoverageParserSubject();

        MethodObserver methodObserver = new MethodObserver();
        methodObserver.setRegistry(map);
        parserSubject.registerObserver(methodObserver);

        CoverageSourceFileNamesObserver sourceFileNamesObserver = new CoverageSourceFileNamesObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
        parserSubject.registerObserver(sourceFileNamesObserver);
        return parserSubject;
    }
    /**
     * Create the complete unit test results parser
     * @param summaryResults
     * @param unitTestFilesregistry
     * @param unitTestResultRegistry
     * @return
     */
    public ParserSubject createUnitTestResultsParser(UnitTestRegistry registry) {
        ParserSubject parser = new ResultsParserSubject();
        
        ResultsObserver resultsObserver = new ResultsObserver();
        resultsObserver.setRegistry(registry.getSummary());
        parser.registerObserver(resultsObserver);
        
        UnitTestResultObserver unitTestResultObserver = new UnitTestResultObserver();
        unitTestResultObserver.setRegistry(registry.getResults());
        parser.registerObserver(unitTestResultObserver);
        
        UnitTestObserver unitTestObserver = new UnitTestObserver();
        unitTestObserver.setRegistry(registry.getResults());
        parser.registerObserver(unitTestObserver);

        return parser;
    }

    /**
     * Creates the complete parser, with the observers registered
     * @param registry initialized registry
     */
    public ParserSubject createOpenCoverParser(SonarCoverage registry) {
        ParserSubject parser = new OpenCoverParserSubject();
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

    public ParserSubject createOpenCoverFileNamesParser(
            MethodToSourceFileIdMap map,
            SourceFileNamesRegistry sourceFileNamesRegistry) {
        ParserSubject parserSubject = new OpenCoverParserSubject();

        OpenCoverMethodObserver methodObserver = new OpenCoverMethodObserver();
        methodObserver.setRegistry(map);
        parserSubject.registerObserver(methodObserver);

        OpenCoverFileNamesAndIdObserver sourceFileNamesObserver = new OpenCoverFileNamesAndIdObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
        parserSubject.registerObserver(sourceFileNamesObserver);
        return parserSubject;
    }
}
