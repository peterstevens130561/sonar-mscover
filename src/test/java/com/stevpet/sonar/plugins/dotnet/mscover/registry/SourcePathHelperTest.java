package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;

public class SourcePathHelperTest {

    @Test
    public void ComparePaths_FileInProject_ShouldBeSame() {
        // Arrange
        String projectFolder = "C:\\Development\\Oahu\\MySolution\\SomeProject";
        String coveragePath = projectFolder + "\\File1.cs";
        String expectedPath = coveragePath;

        assertFileIsInSolution(coveragePath, expectedPath, projectFolder);
    }

    @Test
    public void ComparePaths_IsMatch() {
        // Arrange
        String coveragePath = "C:\\Development\\Oahu\\MySolution\\SomeProject\\File1.cs";
        String localPath = "C:\\stevpet\\Joker\\MySolution\\SomeProject\\File1.cs";
        String solutionPath = "C:\\stevpet\\Joker\\MySolution";
        assertFileIsInSolution(coveragePath, localPath, solutionPath);
                                                        // check is not needed
                                                        // to get the path
    }

    @Test
    public void ComparePaths_CppPathWithWin_IsMatch() {
        // Arrange - paths with whitespace
        String coveragePath = "d:\\structbranch\\jewel.release.oahu.structmod\\jewelearth\\core\\joageometries\\joageometrygroup\\joageometrygroupdefines.h";
        String expectedPath = "C:\\Development\\Jewel.Release.Oahu.StructMod\\JewelEarth\\Core\\joaGeometries"
                + "\\joageometrygroup\\joageometrygroupdefines.h";
        String solutionPath = "C:\\Development\\Jewel.Release.Oahu.StructMod\\JewelEarth\\Core\\joaGeometries";
        assertFileIsInSolution(coveragePath, expectedPath, solutionPath);
    }

    @Test
    public void ComparePaths_WithWhiteSpace_IsMatch() {
        // Arrange - paths with whitespace
        String coveragePath = "\tC:\\Development\\Oahu\\MySolution\\SomeProject\\File1.cs\t\n";
        String expectedPath = "C:\\stevpet\\Joker\\MySolution\\SomeProject\\File1.cs";
        String solutionPath = "C:\\stevpet\\Joker\\MySolution";
        assertFileIsInSolution(coveragePath, expectedPath, solutionPath);
    }

    @Test
    public void ComparePaths_IsNoMatch() {
        String coveragePath = "C:\\Development\\Oahu\\SomeSolution\\SomeProject\\File1.cs";
        String solutionPath = "C:\\stevpet\\Joker\\MySolution";
        SourceFilePathHelper helper = new SourceFilePathHelper();

        helper.setProjectPath(solutionPath);
        helper.setFilePath(coveragePath);
        boolean isModuleInSolution = helper.isModuleInSolution();
        Assert.assertFalse(isModuleInSolution);
    }

    private void assertFileIsInSolution(String coveragePath, String localPath,
            String solutionPath) {
        SourceFilePathHelper helper = new SourceFilePathHelper();

        helper.setProjectPath(solutionPath);
        helper.setFilePath(coveragePath);

        // Act
        String actualPath = helper.getSolutionPath();

        // Assert
        Assert.assertEquals(localPath.toLowerCase(), actualPath);
        Assert.assertTrue(helper.isModuleInSolution()); // later to ensure the
    }
    
    @Test
    public void LowerCaseNameToCanonical_ShouldGiveRealName() throws IOException {
        //Arrange
        File file=TestUtils.getResource("cpp.xml");
        Assert.assertTrue(file.exists());
        //Act
        File canonicalFile=file.getCanonicalFile();
        //Assert
        Assert.assertEquals("Cpp.xml",canonicalFile.getName());
    }
}
