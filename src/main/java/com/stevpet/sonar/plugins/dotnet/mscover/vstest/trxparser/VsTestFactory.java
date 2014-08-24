package com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;

public interface VsTestFactory {

    /**
     * Create the complete unit test results parser
     * @param summaryResults
     * @param unitTestFilesregistry
     * @param unitTestResultRegistry
     * @return
     */
    public abstract XmlParserSubject createUnitTestResultsParser(
            UnitTestRegistry registry);

}