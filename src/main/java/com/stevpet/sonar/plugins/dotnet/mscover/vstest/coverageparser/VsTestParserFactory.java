package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;

public interface VsTestParserFactory {

    /***
     * Creates the standard parser with all registries for the coverage file
     * @param registry
     * @deprecated use {@link #createCoverageParser(VsTestCoverageRegistry, List)} 
     * @return
     */
    XmlParserSubject createCoverageParser(
            VsTestCoverageRegistry registry);
    
    /**
     * Used to be able to find the sourcefile by specifying the method
     * @param map
     * @param sourceFileNamesRegistry
     * @return
     */
   XmlParserSubject createFileNamesParser(MethodToSourceFileIdMap map,
            SourceFileNamesRegistry sourceFileNamesRegistry);

   /***
    * Creates the standard parser with all registries for the coverage file
    * @param registry
    * @param modules to parse
    */
    XmlParserSubject createCoverageParser(VsTestCoverageRegistry registry,
            List<String> modules);


}