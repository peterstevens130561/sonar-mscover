package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.utils.WildcardPattern;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

public class AssembliesFinder {

    private WildcardPattern[] inclusionMatchers;
    private List<String> assemblies ;
    private PropertiesHelper propertiesHelper;

    private AssembliesFinder () {
        
    }
    
    private AssembliesFinder(PropertiesHelper propertiesHelper) {
        this.propertiesHelper=propertiesHelper;
    }
    
    
    public static AssembliesFinder create(PropertiesHelper propertiesHelper) {
        if(propertiesHelper==null) {
            throw new NullPointerException();
        }
        return new AssembliesFinder(propertiesHelper) ;
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
            patterns[i] = patterns[i] + ".dll";
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

    public List<String> findTestProjects(List<VisualStudioProject> projects) {
        if(projects==null || projects.size()==0) {
            throw new SolutionHasNoProjectsSonarException() ;
        }
        assemblies=new ArrayList<String>();
        for(VisualStudioProject project: projects) {
            if(project.isUnitTest()) {
                String assemblyName=project.getAssemblyName();
                String buildConfiguration=propertiesHelper.getRequiredBuildConfiguration();
                String buildPlatform=propertiesHelper.getRequiredBuildPlatform();
                File assemblyFile=project.getArtifact(buildConfiguration, buildPlatform);
                if(assemblyFile==null) {
                    throw new NoAssemblyDefinedMsCoverException(buildConfiguration,buildPlatform);
                }
                assemblies.add(assemblyName);
            }
        }
        return assemblies;       
    }

}
