package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.WildcardPattern;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

public class AssembliesFinder {

    private static Logger LOG = LoggerFactory.getLogger(AssembliesFinder.class);
    
    public static AssembliesFinder create(PropertiesHelper propertiesHelper) {
        if(propertiesHelper==null) {
            throw new NullPointerException();
        }
        return new AssembliesFinder(propertiesHelper) ;
    }
    private WildcardPattern[] inclusionMatchers;
    private List<String> assemblies ;

    private PropertiesHelper propertiesHelper;
       
    
    private AssembliesFinder(PropertiesHelper propertiesHelper) {
        this.propertiesHelper=propertiesHelper;
    }
    
    public List<String> findAssembliesFromConfig(File solutionDirectory, List<VisualStudioProject> projects) {
        String assembliesPattern = propertiesHelper.getUnitTestsAssemblies();
        if(StringUtils.isEmpty(assembliesPattern)) {
            LOG.debug(PropertiesHelper.MSCOVER_UNITTEST_ASSEMBLIES + " undefined, will use projects to find test projects");
            fromBuildConfiguration(projects);
            if(assemblies.isEmpty()) {
                LOG.warn(" no test projects found");
            }
        }  else {      
                fromMSCoverProperty(solutionDirectory);
        }
        return assemblies;
    }
    
    /**
     * Go through the list of projects, and put the full path of each unit test assembly in the returned list
     * @param projects
     * @return list of full paths to unit test assemblies
     * @exception in case of no list or empty list, or no assembly defined for current configuration
     */
    private List<String> fromBuildConfiguration(List<VisualStudioProject> projects) {
        if(projects==null || projects.size()==0) {
            throw new SolutionHasNoProjectsSonarException() ;
        }
        assemblies=new ArrayList<String>();
        for(VisualStudioProject project: projects) {
            if(project.isUnitTest()) {
                addUnitTestAssembly(project);
            }
        }
        return assemblies;       
    }

    private List<String> fromMSCoverProperty(File solutionDirectory) {
        String assembliesPattern = propertiesHelper.getUnitTestsAssemblies();
        if(StringUtils.isEmpty(assembliesPattern)) {
            throw new SonarException(PropertiesHelper.MSCOVER_UNITTEST_ASSEMBLIES + " not set, required though when using this mode");
        }
        setPattern(assembliesPattern);
        findAssemblies(solutionDirectory);
        if(assemblies.isEmpty()) {
            throw new SonarException(" No unittest assemblies found with pattern '" + assembliesPattern + "'");
        }
        return assemblies; 
    }
    
    private void addUnitTestAssembly(VisualStudioProject project) {
        String buildConfiguration=propertiesHelper.getRequiredBuildConfiguration();
        String buildPlatform=propertiesHelper.getRequiredBuildPlatform();
        File assemblyFile=project.getArtifact(buildConfiguration, buildPlatform);
        if(assemblyFile==null) {
            throw new NoAssemblyDefinedMsCoverException(buildConfiguration,buildPlatform);
        }
        String assemblyPath=assemblyFile.getAbsolutePath();
        assemblies.add(assemblyPath);
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

    /**
     * Start finding the patterns defined in setPattern from the specified directory
     * @param directory
     * @return list of absolute paths matching the patterns
     */
    private List<String> findAssemblies(File directory) {
        assemblies=new ArrayList<String>();
        checkDirectory(directory);
        return assemblies;
    }



    /**
     * @param patternSequence commaseperated sequence of patterns to look for
     * ? is any single character
     * * is any number of any characters
     * As we're looking for debug assemblies, the patterns are prefixed with /bin/Debug, and suffixed with .dll
     */
    private void setPattern(String patternSequence) {
        String[] patterns = patternSequence.split(",");
        for(int i=0;i<patterns.length;i++) {
            patterns[i] = patterns[i] + ".dll";
        }
        this.inclusionMatchers=WildcardPattern.create(patterns);         
    }

}
