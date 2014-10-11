package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SonarMsCoverPropertiesLogicTest {
    MsCoverPropertiesStub properties = new MsCoverPropertiesStub();
    MsCoverPropertiesLogic logic = new SonarMsCoverPropertiesLogic(properties);
    @Before
    public void before() {
        
    }
    @Test
    public void createPropertiesLogic_ShouldHaveInstance() {
        assertNotNull(logic);
        boolean actual = logic.isIntegrationTestsEnabled();
        assertFalse(actual);
    }
    
    @Test
    public void integrationTestsPathDefined_IntegrationTestsEnabled() {
        String integrationTestsPath = "aap";
        properties.setIntegrationTestsPath(integrationTestsPath);
        boolean actual=logic.isIntegrationTestsEnabled();
        assertTrue(actual);
    }
    
    @Test
    public void unitTestsPathNotDefined_UnitTestsNotEnabled() {
        String unitTestCoveragePath="";
        properties.setUnitTestCoveragePath(unitTestCoveragePath);
        boolean actual=logic.isUnitTestsEnabled();
        assertFalse(actual);
    }
    @Test
    public void unitTestsPathDefined_UnitTestsEnabled() {
        String unitTestCoveragePath="aap";
        properties.setUnitTestCoveragePath(unitTestCoveragePath);
        boolean actual=logic.isUnitTestsEnabled();
        assertTrue(actual);
    }
}
