package com.stevpet.sonar.plugins.dotnet.mscover.csharpsolutionfilesystem;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.SortedSet;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.scan.filesystem.FileQuery;

public class CSharpSolutionFileSystem implements FileSystem {
    private File solutionDir;
    private CSharpSolutionFileSystem(FileSystem fileSystem) {
        solutionDir = fileSystem.baseDir().getParentFile();  
    }
    public static FileSystem createFromProject(FileSystem fileSystem) {
        return new CSharpSolutionFileSystem(fileSystem);
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

    @Override
    public Charset encoding() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public File workDir() {
        String workingPath = solutionDir.getAbsolutePath() + "\\.sonar";
        return new File(workingPath);
    }
    @Override
    public FilePredicates predicates() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public InputFile inputFile(FilePredicate predicate) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Iterable<InputFile> inputFiles(FilePredicate predicate) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public boolean hasFiles(FilePredicate predicate) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public Iterable<File> files(FilePredicate predicate) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public SortedSet<String> languages() {
        // TODO Auto-generated method stub
        return null;
    }

}
