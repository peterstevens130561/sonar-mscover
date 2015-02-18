/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
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
