package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;

public interface ParserFactory {

    /***
     * Creates the standard parser with all registries for the coverage file
     * @param fileBlocksRegistry
     * @param sourceFileNamesRegistry
     * @return
     */
    public XmlParserSubject createCoverageParser(
            FileBlocksRegistry fileBlocksRegistry,
            SourceFileNamesRegistry sourceFileNamesRegistry);
    
    /**
     * Used to be able to find the sourcefile by specifying the method
     * @param map
     * @param sourceFileNamesRegistry
     * @return
     */
    public XmlParserSubject createFileNamesParser(MethodToSourceFileIdMap map,
            SourceFileNamesRegistry sourceFileNamesRegistry);


}