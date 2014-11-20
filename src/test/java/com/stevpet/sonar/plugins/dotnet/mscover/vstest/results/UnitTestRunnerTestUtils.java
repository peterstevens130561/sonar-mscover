package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesStub;

public class UnitTestRunnerTestUtils {

    private UnitTestRunnerTestUtils() {
        
    }
    public static MsCoverProperties mockUnitTestRunnerSettingsToRun() {
        MsCoverPropertiesStub propertiesHelper = new MsCoverPropertiesStub();
        propertiesHelper.setMode("runvstest");
        propertiesHelper.setRunVsTest(true);
        propertiesHelper.setTestSettings("Testsettings1.testsettings");
        propertiesHelper.setUnitTestAssemblies("**/bin/Debug/*Test*");
        
        propertiesHelper.setUnitTestCoveragePath("coverage.xml");
        
        //when(settings.getString("sonar.mscover.mode")).thenReturn("runvstest");
        //when(settings.getString("sonar.mscover.coveragetool")).thenReturn("vstest");
        //when(settings.getString("sonar.mscover.vstest.testsettings")).thenReturn("Testsettings1.testsettings");
        //when(settings.getString("sonar.mscover.vstest.coverage2xml")).thenReturn("C:/Program Files (x86)/Baker Hughes/Coverage2Xml/CodeCoverage.exe"); 
        //when(settings.getString("sonar.mscover.unittests.assemblies")).thenReturn("**/bin/Debug/*Test*");
        //when(settings.getString("sonar.mscover.unittests.coveragexml")).thenReturn("coverage.xml");
        return propertiesHelper;
    }
}
