/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
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
