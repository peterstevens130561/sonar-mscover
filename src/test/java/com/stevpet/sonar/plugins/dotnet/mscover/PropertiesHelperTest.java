package com.stevpet.sonar.plugins.dotnet.mscover;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertiesHelperTest {

    private Settings settings;
    private MsCoverProperties helper;

    @Before
    public void before() {
        settings = mock(Settings.class);
        helper = PropertiesHelper.create(settings);
    }

    @Test
    public void IntegrationTestsNotSet_ShouldBeDisabled() {
        // Arrange
        String path = null;
        when(
                settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH))
                .thenReturn(path);
        // Act
        boolean enabled = helper.isIntegrationTestsEnabled();
        // Assert
        Assert.assertFalse(enabled);
        Assert.assertFalse(helper.isPluginEnabled());
    }

    @Test
    public void IntegrationTestsBlank_ShouldBeDisabledd() {
        // Arrange
        String path = "";
        when(
                settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH))
                .thenReturn(path);
        // Act
        boolean enabled = helper.isIntegrationTestsEnabled();
        // Assert
        Assert.assertFalse(enabled);
        Assert.assertFalse(helper.isPluginEnabled());
    }

    @Test
    public void IntegrationTestsSet_ShouldBeEnabled() {
        // Arrange
        String path = "a/b/c";
        when(
                settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH))
                .thenReturn(path);
        // Act
        boolean enabled = helper.isIntegrationTestsEnabled();
        // Assert
        Assert.assertTrue(enabled);
        Assert.assertTrue(helper.isPluginEnabled());
    }

    @Test
    public void UnitTestsNotSet_ShouldBeDisabled() {
        // Arrange
        String path = null;
        when(settings.getString(PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH))
                .thenReturn(path);
        // Act
        boolean enabled = helper.isUnitTestsEnabled();
        // Assert
        Assert.assertFalse(enabled);
        Assert.assertFalse(helper.isPluginEnabled());
    }

    @Test
    public void UnitTestsBlank_ShouldBeDisabledd() {
        // Arrange
        String path = "";
        when(settings.getString(PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH))
                .thenReturn(path);
        // Act
        boolean enabled = helper.isUnitTestsEnabled();
        // Assert
        Assert.assertFalse(enabled);
        Assert.assertFalse(helper.isPluginEnabled());
    }

    @Test
    public void UnitTestsSet_ShouldBeEnabled() {
        // Arrange
        String path = "a/b/c";
        when(settings.getString(PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH))
                .thenReturn(path);
        // Act
        boolean enabled = helper.isUnitTestsEnabled();
        // Assert
        Assert.assertTrue(enabled);
        Assert.assertTrue(helper.isPluginEnabled());
    }

    @Test
    public void ExecuteRoot_Empty_ShouldBeDisabled() {
        // Arrange
        String value = "";
        when(settings.getString(PropertiesHelper.MSCOVER_EXECUTEROOT))
                .thenReturn(value);
        // Act
        boolean enabled = helper.excuteRoot();
        // Assert
        Assert.assertFalse(enabled);
    }

    @Test
    public void ExecuteRoot_Null_ShouldBeDisabled() {
        // Arrange
        String value = null;
        when(settings.getString(PropertiesHelper.MSCOVER_EXECUTEROOT))
                .thenReturn(value);
        // Act
        boolean enabled = helper.excuteRoot();
        // Assert
        Assert.assertFalse(enabled);
    }

    @Test
    public void ExecuteRoot_Rubbish_ShouldBeDisabled() {
        // Arrange
        String value = "abc";
        when(settings.getString(PropertiesHelper.MSCOVER_EXECUTEROOT))
                .thenReturn(value);
        // Act
        boolean enabled = helper.excuteRoot();
        // Assert
        Assert.assertFalse(enabled);
    }

    @Test
    public void ExecuteRoot_True_ShouldBeEnabled() {
        // Arrange
        String value = "true";
        when(settings.getString(PropertiesHelper.MSCOVER_EXECUTEROOT))
                .thenReturn(value);
        // Act
        boolean enabled = helper.excuteRoot();
        // Assert
        Assert.assertFalse(enabled);
    }

    @Test
    public void ExecuteRoot_False_ShouldBeDisabled() {
        // Arrange
        String value = "false";
        when(settings.getString(PropertiesHelper.MSCOVER_EXECUTEROOT))
                .thenReturn(value);
        // Act
        boolean enabled = helper.excuteRoot();
        // Assert
        Assert.assertFalse(enabled);
    }

    @Test
    public void defaultRunVsTest_True() {
        setSetting(PropertiesHelper.MSCOVER_MODE, "runvstest");
        setSetting(PropertiesHelper.MSCOVER_COVERAGETOOL, "vstest");
        boolean actual = helper.runVsTest();
        Assert.assertTrue(actual);
    }

    @Test
    public void otherRunVsTest_False() {
        setSetting(PropertiesHelper.MSCOVER_MODE, "runvstest");
        setSetting(PropertiesHelper.MSCOVER_COVERAGETOOL, "bogus");
        boolean actual = helper.runVsTest();
        Assert.assertFalse(actual);
    }

    @Test
    public void defaultRunOpenCover_False() {
        setSetting(PropertiesHelper.MSCOVER_MODE, "runvstest");
        setSetting(PropertiesHelper.MSCOVER_COVERAGETOOL, "vstest");
        boolean actual = helper.runOpenCover();
        Assert.assertFalse(actual);
    }

    @Test
    public void otherRunOpenCover_False() {
        setSetting(PropertiesHelper.MSCOVER_MODE, "runvstest");
        setSetting(PropertiesHelper.MSCOVER_COVERAGETOOL, "bogus");
        boolean actual = helper.runOpenCover();
        Assert.assertFalse(actual);
    }

    @Test
    public void openCoverRunOpenCover_False() {
        setSetting(PropertiesHelper.MSCOVER_MODE, "runvstest");
        setSetting(PropertiesHelper.MSCOVER_COVERAGETOOL, "opencover");
        boolean actual = helper.runOpenCover();
        Assert.assertTrue(actual);
    }

    @Test
    public void hintPathNotSet_Null() {
        String actual = helper.getUnitTestHintPath();
        assertNull(actual);
    }

    @Test
    public void hintPathSet_Value() {
        String path = "C:/Development/Jewel.Release.Oahu/JewelEarth/bin";
        setSetting(PropertiesHelper.MSCOVER_UNITTEST_HINTPATH, path);
        String actual = helper.getUnitTestHintPath();
        assertEquals(path, actual);

    }

    @Test
    public void hintPathMulti_Ignored() {
        String path = "C:/Development/Jewel.Release.Oahu/JewelEarth/bin,SomeOtherPath";
        setSetting(PropertiesHelper.MSCOVER_UNITTEST_HINTPATH, path);
        String actual = helper.getUnitTestHintPath();
        assertEquals(path, actual);
    }

    @Test
    public void givenOpenCoverSkipAutoProps_True() {
        setBoolean(PropertiesHelper.MSCOVER_OPENCOVER_SKIPAUTOPROPS, true);
        boolean actual = helper.getOpenCoverSkipAutoProps();
        assertTrue(actual);
    }

    @Test
    public void givenOpenCoverSkipAutoProps_False() {
        setBoolean(PropertiesHelper.MSCOVER_OPENCOVER_SKIPAUTOPROPS, false);
        boolean actual = helper.getOpenCoverSkipAutoProps();
        assertFalse(actual);
    }

    private void setSetting(String property, String value) {
        when(settings.getString(property)).thenReturn(value);
    }

    private void setBoolean(String property, boolean value) {
        when(settings.getBoolean(property)).thenReturn(value);
    }

}
