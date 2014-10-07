package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;

public class AssembliesFinderTestUtils {

    protected MsCoverProperties propertiesHelper;
    protected AssembliesFinder finder;
    protected List<VisualStudioProject> projects;
    protected VisualStudioProject project;
    private String buildConfiguration = "Debug";
    private String buildPlatform = "AnyCPU";

    public AssembliesFinderTestUtils() {
        super();
    }

    protected void projectIsNotAUnitTestProject() {
        when(project.isUnitTest()).thenReturn(false);
    }

    protected void mockSettingsToUseConfiguration() {
        when(propertiesHelper.getRequiredBuildConfiguration()).thenReturn(buildConfiguration);
        when(propertiesHelper.getRequiredBuildPlatform()).thenReturn(buildPlatform);
    }

    protected void projectIsUnitTestProject(String projectName) {
        when(project.isUnitTest()).thenReturn(true);
        when(project.getArtifactName()).thenReturn(projectName);
    }

    protected void projectDoesNotExistForConfigurationAndPlatform() {
        when(project.getArtifact(eq(buildConfiguration), eq(buildPlatform))).thenReturn(null);
    }

    protected void projectExistsForConfigurationAndPlatform(String unitTestPath) {
        when(project.getArtifact(eq(buildConfiguration), eq(buildPlatform))).thenReturn(new File(unitTestPath));
    }

    protected String getPathToANonExistingDll(String name) {
        File unitTestDll=TestUtils.getResource("AssembliesFinder\\TestDll.txt");
        String unitTestPath=unitTestDll.getParent() + "\\" + name ;
        return unitTestPath;
    }

    protected void addNonExistingDllToIgnoreMissingList(String name) {
        ArrayList<String> missing = new ArrayList<String>();
        missing.add(name);
        when(propertiesHelper.getUnitTestAssembliesThatCanBeIgnoredIfMissing()).thenReturn(missing);
        when(propertiesHelper.isIgnoreMissingUnitTestAssembliesSpecified()).thenReturn(true);
    }

    protected String getPathToExistingUnitTestDll() {
        File unitTestDll=TestUtils.getResource("AssembliesFinder\\TestDll.txt");
        String unitTestPath=unitTestDll.getAbsolutePath();
        return unitTestPath;
    }

    protected List<String> fromBuildConfiguration(List<VisualStudioProject> projects) {
        return finder.findUnitTestAssembliesFromConfig(null, projects);
    }

}