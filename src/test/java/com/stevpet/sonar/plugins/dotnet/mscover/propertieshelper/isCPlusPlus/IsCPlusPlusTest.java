package com.stevpet.sonar.plugins.dotnet.mscover.propertieshelper.isCPlusPlus;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class IsCPlusPlusTest {
    Settings settings;
    MsCoverProperties propertiesHelper;
    private boolean isCPlusPlus;
    
    @Before
    public void before() {
        settings=mock(Settings.class);
        when(settings.getStringArrayBySeparator("sonar.language", ",")).thenCallRealMethod();
        propertiesHelper=PropertiesHelper.create(settings);
    }

    @Test
    public void noLanguageSetting_EmptyList() {
        //Arrange
        //Act
        isCPlusPlus=propertiesHelper.isCPlusPlus();
        //Assert
        assertFalse(isCPlusPlus);
        
    }
       
    @Test
    public void hasCPlusPlus_ExpectTrue() {
        //Arrange
        when(settings.getString("sonar.language")).thenReturn("cs,c++");
        //Act
        isCPlusPlus=propertiesHelper.isCPlusPlus();
        //Assert
        assertTrue(isCPlusPlus);
    }
    
    @Test
    public void hasNotCPlusPlus_ExpectFase() {
        //Arrange
        when(settings.getString("sonar.language")).thenReturn("cs");
        //Act
        isCPlusPlus=propertiesHelper.isCPlusPlus();
        //Assert
        assertFalse(isCPlusPlus);
    }
}
