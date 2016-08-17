package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser;

public class DefaultTestResultsParser implements TestResultsParser {
	
	private UnitTestRegistry registry ;
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser#parse(com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry, java.io.File)
	 */
    @Override
	public void parse(File unitTestResultsFile) {
    	registry = new UnitTestRegistry();
        XmlParserSubject parser = new VsTestResultsParserSubject();
        
        ResultsObserver resultsObserver = new ResultsObserver();
        resultsObserver.setRegistry(registry.getSummary());
        parser.registerObserver(resultsObserver);
        
        UnitTestResultObserver unitTestResultObserver = new UnitTestResultObserver();
        unitTestResultObserver.setRegistry(registry.getTestingResults());
        parser.registerObserver(unitTestResultObserver);
        
        UnitTestDefinitionObserver unitTestDefinitionObserver = new UnitTestDefinitionObserver();
        unitTestDefinitionObserver.setRegistry(registry.getTestingResults());
        parser.registerObserver(unitTestDefinitionObserver);

       parser.parseFile(unitTestResultsFile);
    }

	@Override
	public UnitTestRegistry getUnitTestRegistry() {
		return registry;
	}
}
