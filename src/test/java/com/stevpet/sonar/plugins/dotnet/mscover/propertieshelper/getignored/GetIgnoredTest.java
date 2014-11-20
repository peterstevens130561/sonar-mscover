package com.stevpet.sonar.plugins.dotnet.mscover.propertieshelper.getignored;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;


public class GetIgnoredTest {
    String[] emptyList = {};
    String[] listWithOne = { "one" };
    String[] listWithTwo = { "one","two" };
    
    Settings settings = mock(Settings.class);
    MsCoverProperties propertiesHelper = PropertiesHelper.create(settings);
    @Before
    public void before() {
        
    }
    
    @Test
    public void notSpecified_EmptyList() {
        mockSettingsThenExpect(emptyList,0);
    }
    
    @Test
    public void oneSpecified_ListWithOne() {
        mockSettingsThenExpect(listWithOne,1);
    }
    @Test
    public void twoSpecified_ListWithTwo() {
        mockSettingsThenExpect(listWithTwo,2);
    }
    
    
    private void mockSettingsThenExpect(String []list,int size) {
        when(settings.getStringArrayBySeparator(PropertiesHelper.MSCOVER_IGNOREMISSING_DLL,",")).thenReturn(list);

        Collection<String> ignoreMissing = propertiesHelper.getUnitTestAssembliesThatCanBeIgnoredIfMissing();
        assertNotNull(ignoreMissing);
        assertEquals(size,ignoreMissing.size());      
    }
}
