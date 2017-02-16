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

import org.apache.commons.lang3.StringUtils;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;
import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.VsTestResults;

/**
 *   <TestDefinitions>
    <UnitTest name="TestEqualSurfaces" storage="c:\development\jewel.release.oahu.structmod\jewelearth\core\joageometries\joageometriesunittest\debug\joageometriesunittests.dll" id="6dd2b593-aa2d-3a7b-1a71-f15debf90d70">
      <Execution id="33fc91bf-404f-494e-bd05-34cdc1b6af55" />
      <TestMethod codeBase="c:\Development\Jewel.Release.Oahu.StructMod\JewelEarth\Core\joaGeometries\joaGeometriesUnitTest\Debug\joaGeometriesUnitTests.dll" adapterTypeName="Microsoft.VisualStudio.TestTools.TestTypes.Unit.UnitTestAdapter" className="joaGeometriesUnitTest.joaGeometriesUnitTests" name="TestEqualSurfaces" />
    </UnitTest>
 * @author stevpet
 *
 */
public class UnitTestDefinitionObserver implements ParserObserver {

    private VsTestResults vsTestResults;
    private UnitTestMethodResult unitTestResult;
    public void setRegistry(VsTestResults vsTestResults) {
        this.vsTestResults = vsTestResults;
    }
    
    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
        registrar.inPath("TestDefinitions").inElement("UnitTest").onAttribute("id", this::id);
        
        registrar.inPath("TestDefinitions/UnitTest").inElement("TestMethod")
            .onAttribute("codeBase", this::codeBase)
            .onAttribute("className", this::className)
            .onAttribute("name",this::name);
    }
    
    public void id(String testId) {
        unitTestResult=vsTestResults.getById(testId);
        if(unitTestResult==null) {
            unitTestResult = vsTestResults.add(testId);
        }
    }
    
    public void codeBase(String value) {
        unitTestResult.setCodeBase(value);
        unitTestResult.setModuleFromCodeBase(value);
    }
    

    public void className(String value) {
        unitTestResult.setClassName(value);
        unitTestResult.setNamespaceNameFromClassName(value);
        
    }
    
    public void name(String value) {
        String currentName=unitTestResult.getTestName();
        if(StringUtils.isEmpty(currentName)) {
            unitTestResult.setTestName(value);
        }
        if(!value.equals(unitTestResult.getTestName())) {
            throw new IllegalStateException("Name differs " + value );
        }
    }


}
