package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

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
import com.stevpet.sonar.plugins.dotnet.mscover.exception.NoAssemblyDefinedMsCoverException;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.SolutionHasNoProjectsSonarException;

public abstract class AbstractAssembliesFinder implements AssembliesFinder {

    private static Logger LOG = LoggerFactory.getLogger(DefaultAssembliesFinder.class);

    public static AssembliesFinder create(PropertiesHelper propertiesHelper) {
        AssembliesFinder finder;
        if(propertiesHelper!=null && propertiesHelper.isIgnoreMissingUnitTestAssembliesSpecified()) {
            finder=new IgnoreMissingAssembliesFinder(propertiesHelper) ;
        } else {
            finder=new DefaultAssembliesFinder(propertiesHelper) ;
        }
        return finder;
    }

    private WildcardPattern[] inclusionMatchers;
    private List<String> assemblies;
    protected PropertiesHelper propertiesHelper;

    public AbstractAssembliesFinder(PropertiesHelper propertiesHelper) {
        this.propertiesHelper=propertiesHelper;
    }

    public List<String> findUnitTestAssembliesFromConfig(File solutionDirectory, List<VisualStudioProject> projects) {
        String assembliesPattern = propertiesHelper.getUnitTestsAssemblies();
        if(StringUtils.isEmpty(assembliesPattern)) {
            fromBuildConfiguration(projects);
        }  else {      
            fromMSCoverProperty(solutionDirectory);
        }
        if(assemblies.isEmpty()) {
            LOG.warn(" no test projects found");
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
        if(!StringUtils.isEmpty(buildPlatform)) {
            buildPlatform=buildPlatform.replace(" ", "");
        }
        File assemblyFile=project.getArtifact(buildConfiguration, buildPlatform);
        if(assemblyFile==null) {
            throw new NoAssemblyDefinedMsCoverException(buildConfiguration,buildPlatform);
        }
        String assemblyPath=assemblyFile.getAbsolutePath();
        if(!assemblyFile.exists()) {
            LOG.info("Unit test assembly " + assemblyPath + " does not exist Check .proj file for " + buildPlatform + "|" + buildConfiguration);
            assemblyFile =createPathToUnitTestFile(project,buildConfiguration);
            LOG.info("Unit test assembly defaulting to " + assemblyPath);
        }
        if(!assemblyFile.exists() ) {
            onNonExistingFile(assemblyFile);            
        }
        assemblyPath= assemblyFile.getAbsolutePath();
        if(assemblyFile.exists()) {
            assemblies.add(assemblyPath);
        }
    }

    private File createPathToUnitTestFile(VisualStudioProject project, String buildConfiguration) {
        File artifactDirectory=project.getDirectory();
        String artifactPath = "bin/" + buildConfiguration + "/" + project.getArtifactName();
        return new File(artifactDirectory, artifactPath);
    }

    public void checkDirectory(File directory) {
        for(File file :directory.listFiles()) {
            checkFile(file);
        }
    }

    private void checkFile(File file) {
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