/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.VsTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser;

public class DefaultTestResultsParser implements TestResultsParser {
	
    private final VsTestResults unitTestingResults;
    public DefaultTestResultsParser(VsTestResults unitTestingResults) {
        this.unitTestingResults=unitTestingResults;
    }

    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser#parse(com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry, java.io.File)
	 */
    @Override
	public void parse(File unitTestResultsFile) {
        XmlParser parser = new DefaultXmlParser();
       
        UnitTestResultObserver unitTestResultObserver = new UnitTestResultObserver();
        unitTestResultObserver.setRegistry(unitTestingResults);
        parser.registerObserver(unitTestResultObserver);
        
        UnitTestDefinitionObserver unitTestDefinitionObserver = new UnitTestDefinitionObserver();
        unitTestDefinitionObserver.setRegistry(unitTestingResults);
        parser.registerObserver(unitTestDefinitionObserver);

       parser.parseFile(unitTestResultsFile);
    }

}
