package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestResultRegistry;

public interface ParserFactory {

    /***
     * Creates the standard parser with all registries for the coverage file
     * @param fileBlocksRegistry
     * @param sourceFileNamesRegistry
     * @return
     */
    public ParserSubject createCoverageParser(
            FileBlocksRegistry fileBlocksRegistry,
            SourceFileNamesRegistry sourceFileNamesRegistry);
    
    /**
     * Create the parser for the unit test results file (.trx)
     * @param registry
     * @return
     */
    public ParserSubject createUnitTestResultsParser(UnitTestRegistry registry);
    /**
     * Used to be able to find the sourcefile by specifying the method
     * @param map
     * @param sourceFileNamesRegistry
     * @return
     */
    public ParserSubject createFileNamesParser(MethodToSourceFileIdMap map,
            SourceFileNamesRegistry sourceFileNamesRegistry);


}