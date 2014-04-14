package com.stevpet.sonar.plugins.dotnet.mscover.parser.results;

import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestResultRegistry;

/**
 *   <TestDefinitions>
    <UnitTest name="TestEqualSurfaces" storage="c:\development\jewel.release.oahu.structmod\jewelearth\core\joageometries\joageometriesunittest\debug\joageometriesunittests.dll" id="6dd2b593-aa2d-3a7b-1a71-f15debf90d70">
      <Execution id="33fc91bf-404f-494e-bd05-34cdc1b6af55" />
      <TestMethod codeBase="c:\Development\Jewel.Release.Oahu.StructMod\JewelEarth\Core\joaGeometries\joaGeometriesUnitTest\Debug\joaGeometriesUnitTests.dll" adapterTypeName="Microsoft.VisualStudio.TestTools.TestTypes.Unit.UnitTestAdapter" className="joaGeometriesUnitTest.joaGeometriesUnitTests" name="TestEqualSurfaces" />
    </UnitTest>
 * @author stevpet
 *
 */
public class UnitTestObserver extends BaseParserObserver {

    public UnitTestObserver() {
        setPattern("TestDefinitions/(UnitTest|UnitTest/TestMethod)");
    }
    
    private UnitTestResultRegistry registry;
    private UnitTestResultModel unitTestResult;
    public void setRegistry(UnitTestResultRegistry registry) {
        this.registry = registry;
    }
    
    @AttributeMatcher(attributeName = "id", elementName = "UnitTest")
    public void id(String value) {
        unitTestResult=registry.getById(value);
    }
    
    @AttributeMatcher(attributeName="codeBase",elementName="TestMethod")
    public void codeBase(String value) {
        unitTestResult.setCodeBase(value);
        unitTestResult.setModuleFromCodeBase(value);
    }
    
    @AttributeMatcher(attributeName="className",elementName="TestMethod") 
    public void className(String value) {
        unitTestResult.setClassName(value);
        unitTestResult.setNamespaceNameFromClassName(value);
        
    }
    
    @AttributeMatcher(attributeName="name",elementName="TestMethod")
    public void name(String value) {
        if(!value.equals(unitTestResult.getTestName())) {
            throw new SonarException("Name differs " + value );
        }
    }
}
