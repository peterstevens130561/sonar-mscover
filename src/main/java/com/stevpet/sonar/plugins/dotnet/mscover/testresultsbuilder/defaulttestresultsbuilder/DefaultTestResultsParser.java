/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestingResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser;

public class DefaultTestResultsParser implements TestResultsParser {
	
	private UnitTestingResults unitTestsingResults ;
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser#parse(com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry, java.io.File)
	 */
    @Override
	public void parse(File unitTestResultsFile) {
    	unitTestsingResults = new UnitTestingResults();
        XmlParser parser = new DefaultXmlParser();
        
        
        UnitTestResultObserver unitTestResultObserver = new UnitTestResultObserver();
        unitTestResultObserver.setRegistry(unitTestsingResults);
        parser.registerObserver(unitTestResultObserver);
        
        UnitTestDefinitionObserver unitTestDefinitionObserver = new UnitTestDefinitionObserver();
        unitTestDefinitionObserver.setRegistry(unitTestsingResults);
        parser.registerObserver(unitTestDefinitionObserver);

       parser.parseFile(unitTestResultsFile);
    }

	@Override
	public UnitTestingResults getUnitTestingResults() {
		return unitTestsingResults;
	}
}
