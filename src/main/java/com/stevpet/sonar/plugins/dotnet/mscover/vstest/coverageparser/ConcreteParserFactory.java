package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser;


import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.CoverageSourceFileNamesToSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.CoverageMethodBlocksToFileBlocksObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.MethodToSourceFileIdMapObserver;


public class ConcreteParserFactory implements ParserFactory {
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.CoverageParserFactory#createDefault(com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry, com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry)
     */
    public XmlParserSubject createCoverageParser(FileBlocksRegistry fileBlocksRegistry,
            SourceFileNamesRegistry sourceFileNamesRegistry) {

        XmlParserSubject parserSubject = new CoverageParserSubject();

        CoverageMethodBlocksToFileBlocksObserver methodBlocksObserver = new CoverageMethodBlocksToFileBlocksObserver();
        methodBlocksObserver.setRegistry(fileBlocksRegistry);
        parserSubject.registerObserver(methodBlocksObserver);

        CoverageSourceFileNamesToSourceFileNamesObserver sourceFileNamesObserver = new CoverageSourceFileNamesToSourceFileNamesObserver();
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
    public XmlParserSubject createFileNamesParser(MethodToSourceFileIdMap map,
            SourceFileNamesRegistry sourceFileNamesRegistry) {

        XmlParserSubject parserSubject = new CoverageParserSubject();

        MethodToSourceFileIdMapObserver methodObserver = new MethodToSourceFileIdMapObserver();
        methodObserver.setRegistry(map);
        parserSubject.registerObserver(methodObserver);

        CoverageSourceFileNamesToSourceFileNamesObserver sourceFileNamesObserver = new CoverageSourceFileNamesToSourceFileNamesObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
        parserSubject.registerObserver(sourceFileNamesObserver);
        return parserSubject;
    }
 

}
