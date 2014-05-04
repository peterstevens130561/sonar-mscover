package com.stevpet.sonar.plugins.dotnet.mscover.propertieshelper.getrunmode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

import static org.mockito.Mockito.mock ;
import static org.mockito.Mockito.when ;

public class GetRunModeTest {
    
    private Settings settings;
    private PropertiesHelper propertiesHelper ;
    @Before
    public void before() {
        settings = mock(Settings.class);
        propertiesHelper = PropertiesHelper.create(settings);
    }
    
    @Test
    public void settingNotSpecified_Skip() {
        test(RunMode.SKIP,null);
    }
    
    @Test
    public void settingEmpty_Skip() {
        test(RunMode.SKIP,"");
    }
    
    @Test(expected=MsCoverException.class)
    public void settingInvalid_Exception() {
        test(RunMode.SKIP,"booh");
    }
 
    @Test
    public void settingMixedCaseReuse_REUSE() {
        test(RunMode.REUSE,"rEuSe");
    }
    
    @Test
    public void settingRunVsTest_RUNVSTEST() {
        //Arrange
        test(RunMode.RUNVSTEST,"runvstest");
    }
    
    @Test
    public void settingSkipt_SKIP() {
        test(RunMode.SKIP,"skip");
    }
    
    private void test(RunMode expected,String propertyValue) {
        when(settings.getString(PropertiesHelper.MSCOVER_MODE)).thenReturn(propertyValue);       
        RunMode runMode=propertiesHelper.getRunMode();
        //Assert
        Assert.assertEquals(expected, runMode);
    }
}
