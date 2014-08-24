package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser;


import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestLinesToCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestSourceFileNamesToCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestSourceFileNamesToSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestMethodBlocksToFileBlocksObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestMethodToSourceFileIdMapObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestCoverageObserver;


public class ConcreteParserFactory implements ParserFactory {
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.CoverageParserFactory#createDefault(com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry, com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry)
     */
    public XmlParserSubject createCoverageParser(VsTestRegistry registry) {

        XmlParserSubject parserSubject = new CoverageParserSubject();

        VsTestCoverageObserver[] observers = {
                new VsTestSourceFileNamesToCoverageObserver(),
                new VsTestLinesToCoverageObserver(),
                new VsTestMethodBlocksToFileBlocksObserver(),
                new VsTestSourceFileNamesToSourceFileNamesObserver()
        };
        
        for(VsTestCoverageObserver observer : observers) {
            observer.setVsTestRegistry(registry);
            parserSubject.registerObserver(observer);            
        }
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

        VsTestMethodToSourceFileIdMapObserver methodObserver = new VsTestMethodToSourceFileIdMapObserver();
        methodObserver.setRegistry(map);
        parserSubject.registerObserver(methodObserver);

        VsTestSourceFileNamesToSourceFileNamesObserver sourceFileNamesObserver = new VsTestSourceFileNamesToSourceFileNamesObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
        parserSubject.registerObserver(sourceFileNamesObserver);
        return parserSubject;
    }
 

}
