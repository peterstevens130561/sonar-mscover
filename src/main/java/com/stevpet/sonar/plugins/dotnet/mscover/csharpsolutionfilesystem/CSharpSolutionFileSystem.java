package com.stevpet.sonar.plugins.dotnet.mscover.csharpsolutionfilesystem;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

public class CSharpSolutionFileSystem implements ModuleFileSystem {
    private File solutionDir;
    private CSharpSolutionFileSystem(ModuleFileSystem moduleFileSystem) {
        solutionDir = moduleFileSystem.baseDir().getParentFile();  
    }
    public static ModuleFileSystem createFromProject(ModuleFileSystem moduleFileSystem) {
        return new CSharpSolutionFileSystem(moduleFileSystem);
    }
    public File baseDir() {
        return this.solutionDir;
    }

    public File buildDir() {

        return null;
    }

    public List<File> sourceDirs() {
        return null;
    }

    public List<File> testDirs() {
        return null;
    }

    public List<File> binaryDirs() {
       return null;
    }

    public List<File> files(FileQuery query) {
        return null;
    }

    public Charset sourceCharset() {
        return null;
    }

    public File workingDir() {
        String workingPath = solutionDir.getAbsolutePath() + "\\.sonar";
        return new File(workingPath);
                
    }

}
