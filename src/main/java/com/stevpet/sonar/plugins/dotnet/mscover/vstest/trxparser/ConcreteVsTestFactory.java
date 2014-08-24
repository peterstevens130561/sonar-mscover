package com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;

public class ConcreteVsTestFactory implements VsTestFactory {
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser.VsTestFactory#createUnitTestResultsParser(com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry)
     */
    public XmlParserSubject createUnitTestResultsParser(UnitTestRegistry registry) {
        XmlParserSubject parser = new ResultsParserSubject();
        
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
}
