package com.stevpet.sonar.plugins.dotnet.mscover.propertieshelper.getlanguages;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetLanguagesTest {
    
    Settings settings;
    PropertiesHelper propertiesHelper;
    private List<String> languages;
    @Before
    public void before() {
        settings=mock(Settings.class);
        when(settings.getStringArrayBySeparator("sonar.language", ",")).thenCallRealMethod();
        propertiesHelper=PropertiesHelper.create(settings);
    }

    @Test
    public void NoLanguageSetting_EmptyList() {
        //Arrange
        //Act
        languages=propertiesHelper.getLanguages();
        //Assert
        assertEquals(0, languages.size());
        
    }
    
    @Test
    public void EmptyLanguageSetting_EmptyList() {
        //Arrange
        when(settings.getString("sonar.language")).thenReturn("");
        //Act
        languages=propertiesHelper.getLanguages();
        //Assert
        assertEquals(0, languages.size());
        
    }
    
    @Test
    public void OneLanguageSetting_ListHasOne() {
        //Arrange
        when(settings.getString("sonar.language")).thenReturn("cs");
        //Act
        languages=propertiesHelper.getLanguages();       
        //Assert
        assertEquals(1, languages.size());
        assertEquals("cs",languages.get(0));
    }
    
    @Test
    public void OneLanguageSetting_ListHasTwo() {
        //Arrange
        when(settings.getString("sonar.language")).thenReturn("cs,c++");
        //Act
        languages=propertiesHelper.getLanguages();       
        //Assert
        assertEquals(2, languages.size());
        assertEquals("cs",languages.get(0));
        assertEquals("c++",languages.get(1));
    }
}
