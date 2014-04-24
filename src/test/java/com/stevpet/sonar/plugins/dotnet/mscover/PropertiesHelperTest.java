package com.stevpet.sonar.plugins.dotnet.mscover;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class PropertiesHelperTest {

    private Settings settings ;
    private PropertiesHelper helper ;
    
    @Before
    public void before() {
        settings = mock(Settings.class);
        helper = new PropertiesHelper(settings);
    }
    
    @Test
    public void IntegrationTestsNotSet_ShouldBeDisabled () {
        //Arrange
        String path=null;
        when(settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH)).thenReturn(path);
        //Act
        boolean enabled=helper.isIntegrationTestsEnabled();
        //Assert
        Assert.assertFalse(enabled);
        Assert.assertFalse(helper.isPluginEnabled());
    }
    @Test
    public void IntegrationTestsBlank_ShouldBeDisabledd() {
        //Arrange
        String path = "";
        when(settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH)).thenReturn(path);
        //Act
        boolean enabled=helper.isIntegrationTestsEnabled();
        //Assert
        Assert.assertFalse(enabled);
        Assert.assertFalse(helper.isPluginEnabled());
    }
    @Test
    public void IntegrationTestsSet_ShouldBeEnabled() {
        //Arrange
        String path = "a/b/c";
        when(settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH)).thenReturn(path);
        //Act
        boolean enabled=helper.isIntegrationTestsEnabled();
        //Assert
        Assert.assertTrue(enabled);
        Assert.assertTrue(helper.isPluginEnabled());
    }
    
    @Test
    public void UnitTestsNotSet_ShouldBeDisabled () {
        //Arrange
        String path=null;
        when(settings.getString(PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH)).thenReturn(path);
        //Act
        boolean enabled=helper.isUnitTestsEnabled();
        //Assert
        Assert.assertFalse(enabled);
        Assert.assertFalse(helper.isPluginEnabled());
    }
    @Test
    public void UnitTestsBlank_ShouldBeDisabledd() {
        //Arrange
        String path = "";
        when(settings.getString(PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH)).thenReturn(path);
        //Act
        boolean enabled=helper.isUnitTestsEnabled();
        //Assert
        Assert.assertFalse(enabled);
        Assert.assertFalse(helper.isPluginEnabled());
    }
    @Test
    public void UnitTestsSet_ShouldBeEnabled() {
        //Arrange
        String path = "a/b/c";
        when(settings.getString(PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH)).thenReturn(path);
        //Act
        boolean enabled=helper.isUnitTestsEnabled();
        //Assert
        Assert.assertTrue(enabled);
        Assert.assertTrue(helper.isPluginEnabled());
    }
    
    @Test public  void ExecuteRoot_Empty_ShouldBeDisabled() {
        //Arrange
        String value = "";
        when(settings.getString(PropertiesHelper.MSCOVER_EXECUTEROOT)).thenReturn(value);
        //Act
        boolean enabled=helper.excuteRoot();
        //Assert
        Assert.assertFalse(enabled);      
    }
   
    @Test public void ExecuteRoot_Null_ShouldBeDisabled() {
        //Arrange
        String value = null;
        when(settings.getString(PropertiesHelper.MSCOVER_EXECUTEROOT)).thenReturn(value);
        //Act
        boolean enabled=helper.excuteRoot();
        //Assert
        Assert.assertFalse(enabled);      
    }   
    @Test public void ExecuteRoot_Rubbish_ShouldBeDisabled() {
        //Arrange
        String value = "abc";
        when(settings.getString(PropertiesHelper.MSCOVER_EXECUTEROOT)).thenReturn(value);
        //Act
        boolean enabled=helper.excuteRoot();
        //Assert
        Assert.assertFalse(enabled);      
    }
    
    @Test public void ExecuteRoot_True_ShouldBeEnabled() {
        //Arrange
        String value = "true";
        when(settings.getString(PropertiesHelper.MSCOVER_EXECUTEROOT)).thenReturn(value);
        //Act
        boolean enabled=helper.excuteRoot();
        //Assert
        Assert.assertFalse(enabled);      
    }
    
    @Test public void ExecuteRoot_False_ShouldBeDisabled() {
        //Arrange
        String value = "false";
        when(settings.getString(PropertiesHelper.MSCOVER_EXECUTEROOT)).thenReturn(value);
        //Act
        boolean enabled=helper.excuteRoot();
        //Assert
        Assert.assertFalse(enabled);      
    }
}
