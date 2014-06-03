package com.stevpet.sonar.plugins.dotnet.mscover.helpers.sonarresourcehelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.helpers.SonarResourceHelper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.sonar.test.TestUtils.getResource;

@SuppressWarnings("deprecation")
public class GetFromFileTest {

    Project project ;
    ProjectFileSystem projectFileSystem;
    File file;
    List<File> sourceDirs = new ArrayList<File>() ;
    @Before
    public void Before() {
        project = mock(Project.class);
        projectFileSystem=mock(ProjectFileSystem.class);
        when(project.getFileSystem()).thenReturn(projectFileSystem);
        when(projectFileSystem.getSourceDirs()).thenReturn(sourceDirs);
    }
    
    @Test
    public void NoSourceDir_FileNotFound() {
        //Arrange
        sourceDirs = new ArrayList<File>() ;
        //Act
        File notInProjectFile = new File("a/b/c/d.cs");
        org.sonar.api.resources.File fileResource=SonarResourceHelper.getFromFile(notInProjectFile, project);
        //Assert
        Assert.assertNull(fileResource);
    }
    @Test
    public void EmptyProject_FileNotFound() {
        //Arrange
        addSourceDir("EmptyProject");
        //Act
        File notInProjectFile = new File("a/b/c/d.cs");
        org.sonar.api.resources.File fileResource=SonarResourceHelper.getFromFile(notInProjectFile, project);
        //Assert
        Assert.assertNull(fileResource);
    }
    
    @Test
    public void RealProject_FileInSourceDir1() {
        //Arrange
        String sourcePath="ProjectWithOneSourceDir/SourceDir1";
        addSourceDir(sourcePath);
        File notInProjectFile = getSourceFile(sourcePath,"file20.cs");
        ///Act
        org.sonar.api.resources.File fileResource=SonarResourceHelper.getFromFile(notInProjectFile, project);
        //Assert
        Assert.assertNotNull(fileResource);
        Assert.assertEquals("file20.cs",fileResource.getKey());
    }
    
    @Test
    public void RealProject_FileInSourceDir2() {
        //Arrange
        String sourcePath1 = "ProjectWithTwoSourceDirs/SourceDir1";
        String sourcePath2 = "ProjectWithTwoSourceDirs/SourceDir2";
        addSourceDir(sourcePath1);
        addSourceDir(sourcePath2);
        File fileInDir2=getSourceFile(sourcePath2,"somefile.cs");
        //Act
        org.sonar.api.resources.File fileResource=SonarResourceHelper.getFromFile(fileInDir2, project);
        //Assert
        Assert.assertNotNull(fileResource);
        Assert.assertEquals("somefile.cs", fileResource.getKey());
        
    }
    
    @Test
    public void RealProject_FileInHierarchy() {
        //Arrange
        String sourcePath1 = "ProjectWithTwoSourceDirs/SourceDir1";
        String sourcePath2 = "ProjectWithTwoSourceDirs/SourceDir2";
        addSourceDir(sourcePath1);
        addSourceDir(sourcePath2);
        File fileInDir2=getSourceFile(sourcePath2,"mydir/somefile.cs");
        //Act
        org.sonar.api.resources.File fileResource=SonarResourceHelper.getFromFile(fileInDir2, project);
        //Assert
        Assert.assertNotNull(fileResource);
        Assert.assertEquals("mydir/somefile.cs", fileResource.getKey());
        Assert.assertEquals("somefile.cs",fileResource.getName());
        
    }
    private void addSourceDir(String resourcePath) {
        File sourceDir=getTestResource(resourcePath);
        sourceDirs.add(sourceDir);
    }
    private File getTestResource(String resourcePath) {
        return TestUtils.getResource("SonarResourceHelper/" + resourcePath);
    }
    
    private File getSourceFile(String path,String name) {
        File sourceDir=getTestResource(path);
        File sourceFile = new File(sourceDir + "/" + name);
        return sourceFile;
    }
    
    
}
