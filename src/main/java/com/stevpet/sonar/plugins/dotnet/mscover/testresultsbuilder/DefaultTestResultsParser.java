package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser.ResultsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser.ResultsParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser.UnitTestObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser.UnitTestResultObserver;

public class DefaultTestResultsParser implements TestResultsParser {
	
	private UnitTestRegistry registry ;
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser#parse(com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry, java.io.File)
	 */
    @Override
	public void parse(File unitTestResultsFile) {
    	registry = new UnitTestRegistry();
        XmlParserSubject parser = new ResultsParserSubject();
        
        ResultsObserver resultsObserver = new ResultsObserver();
        resultsObserver.setRegistry(registry.getSummary());
        parser.registerObserver(resultsObserver);
        
        UnitTestResultObserver unitTestResultObserver = new UnitTestResultObserver();
        unitTestResultObserver.setRegistry(registry.getTestingResults());
        parser.registerObserver(unitTestResultObserver);
        
        UnitTestObserver unitTestObserver = new UnitTestObserver();
        unitTestObserver.setRegistry(registry.getTestingResults());
        parser.registerObserver(unitTestObserver);

       parser.parseFile(unitTestResultsFile);
    }

	@Override
	public UnitTestRegistry getUnitTestRegistry() {
		// TODO Auto-generated method stub
		return registry;
	}
}
