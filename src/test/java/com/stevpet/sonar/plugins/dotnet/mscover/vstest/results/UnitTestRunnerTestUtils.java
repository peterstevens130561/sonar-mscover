package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.sonar.api.config.Settings;

public class UnitTestRunnerTestUtils {

    private UnitTestRunnerTestUtils() {
        
    }
    public static Settings mockUnitTestRunnerSettingsToRun() {
        Settings settings = mock(Settings.class);
        when(settings.getString("sonar.mscover.mode")).thenReturn("runvstest");
        when(settings.getString("sonar.mscover.vstest.testsettings")).thenReturn("Testsettings1.testsettings");
        when(settings.getString("sonar.mscover.vstest.coverage2xml")).thenReturn("C:/Program Files (x86)/Baker Hughes/Coverage2Xml/CodeCoverage.exe"); 
        when(settings.getString("sonar.mscover.unittests.assemblies")).thenReturn("*Test*");
        when(settings.getString("sonar.mscover.unittests.coveragexml")).thenReturn("coverage.xml");
        return settings;
    }
}
