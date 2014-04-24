package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.utils.WildcardPattern;


public class AssembliesFinder {

    private WildcardPattern[] inclusionMatchers;
    private List<String> assemblies ;

    private AssembliesFinder () {
        
    }
    
    public static AssembliesFinder create() {
        return new AssembliesFinder() ;
    }
    /**
     * @param patternSequence commaseperated sequence of patterns to look for
     * ? is any single character
     * * is any number of any characters
     * As we're looking for debug assemblies, the patterns are prefixed with /bin/Debug, and suffixed with .dll
     */
    public void setPattern(String patternSequence) {
        String[] patterns = patternSequence.split(",");
        for(int i=0;i<patterns.length;i++) {
            patterns[i] = "**/bin/Debug/" + patterns[i] + ".dll";
        }
        this.inclusionMatchers=WildcardPattern.create(patterns);         
    }

    /**
     * Start finding the patterns defined in setPattern from the specified directory
     * @param directory
     * @return list of absolute paths matching the patterns
     */
    public List<String> findAssemblies(File directory) {
        assemblies=new ArrayList<String>();
        checkDirectory(directory);
        return assemblies;
    }
    public void checkDirectory(File directory)  {
        for(File file :directory.listFiles()) {
            checkFile(file);
        }
    }

    private void checkFile(File file)  {
        if(file.isDirectory()) {
            checkDirectory(file);
        } else if(file.isFile()) {
            String absolutePath=file.getAbsolutePath().replaceAll("\\\\", "/");
            if (WildcardPattern.match(inclusionMatchers,absolutePath)) {
                assemblies.add(absolutePath);
            }
        }
    }

}
