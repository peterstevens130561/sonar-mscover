/*
 * SonarQube MSCover coverage plugin
 * Copyright (C) 2014 Peter Stevens
 * peter@famstevens.eu
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
 */
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
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.dotnetutils.UnitTestProjectFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.NoAssemblyDefinedMsCoverException;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.SolutionHasNoProjectsSonarException;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.Environment.Result;

public abstract class AbstractAssembliesFinder implements AssembliesFinder {

    private static Logger LOG = LoggerFactory.getLogger(AbstractAssembliesFinder.class);

    private WildcardPattern[] inclusionMatchers;
    private List<String> assemblies;
    protected MsCoverProperties propertiesHelper;
    private UnitTestProjectFinder unitTestProjectFinder=new UnitTestProjectFinder();

    private Environment environment;
    
    public AbstractAssembliesFinder(MsCoverProperties propertiesHelper) {
        this.propertiesHelper=propertiesHelper;
    }

    public String findUnitTestAssembliesDir(File startDirectory) {
        List<String> assemblies = findUnitTestAssembliesFromConfig(startDirectory);
        File firstAssembly= new File(assemblies.get(0));
        return firstAssembly.getParent();
        
        
    }
    /**
     * If no assembliesPattern is defined, then find through the projects, otherwise use the pattern starting in the
     * solutiondirectory
     * 
     * @exception SonarException is thrown when no assemblies are found while pattern is defined.
     * @return the list of assemblies
     * 
     * exception is thrown when no assemblies are found while pattern is defined.
     * @deprecated as of now, migrate to {@link #findUnitTestAssembliesFromConfig(File)}
     * 
     */
    @Deprecated
    public List<String> findUnitTestAssembliesFromConfig(File solutionDirectory, List<VisualStudioProject> projects) {
        String assembliesPattern = propertiesHelper.getUnitTestsAssemblies();
        if(StringUtils.isEmpty(assembliesPattern)) {
            //fromBuildConfiguration(projects);
            fromVisualStudioProperty(solutionDirectory);
        }  else {      
            fromMSCoverProperty(solutionDirectory);
        }
        if(assemblies.isEmpty()) {
            LOG.warn(" no test projects found");
        }
        return assemblies;
    }
    
    public List<String> findUnitTestAssembliesFromConfig(File solutionDirectory) {
        String assembliesPattern = propertiesHelper.getUnitTestsAssemblies();
        if(StringUtils.isEmpty(assembliesPattern)) {
            //fromBuildConfiguration(projects);
            fromVisualStudioProperty(solutionDirectory);
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
    private void fromBuildConfiguration(List<VisualStudioProject> projects) {
        if(projects==null || projects.size()==0) {
            throw new SolutionHasNoProjectsSonarException() ;
        }
        assemblies=new ArrayList<String>();
        for(VisualStudioProject project: projects) {
            if(project.isUnitTest()) {
                addUnitTestAssembly(project);
            }
        }     
    }

private void fromVisualStudioProperty(File solutionDirectory) {
        String solutionName=propertiesHelper.getSolutionName();
        String unitTestPattern=propertiesHelper.getVisualStudioUnitTestPattern();
        List<File> projectDirectories=unitTestProjectFinder
                .setStartDirectory(solutionDirectory)
                .gotoDirWithSolution(solutionName)
                .findUnitTestProjectDirectories(unitTestPattern);
        
        String buildConfiguration=propertiesHelper.getRequiredBuildConfiguration();
        
        assemblies=new ArrayList<String>();  
        for(File projectDirectory:projectDirectories) {
            String assemblyName = projectDirectory.getName() + ".dll";
            environment.setResult(Result.Check);
            searchNonExistingFile(projectDirectory,assemblyName,buildConfiguration);            

            if(environment.getResult() == Result.Check && environment.getAssembly().exists()) {
                assemblies.add(environment.getAssembly().getAbsolutePath());
            }
        }
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

        if(!assemblyFile.exists() ) {
            String assemblyName=assemblyFile.getName();
            File projectDir=project.getDirectory();
            searchNonExistingFile(projectDir,assemblyName,buildConfiguration);
            if(environment.exists()) {
                assemblyFile=environment.getAssembly();
            }
        }

        if(assemblyFile!=null && assemblyFile.exists()) {
            assemblies.add(assemblyFile.getAbsolutePath());
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
    
    /**
     * testing purposes only
     * @param unitTestProjectFinder
     */
    public void setUnitTestProjectFinder(UnitTestProjectFinder unitTestProjectFinder) {
        this.unitTestProjectFinder = unitTestProjectFinder;
    }

    public void setEnvironment(Environment finderResult) {
        this.environment= finderResult;
    }
    
    public Environment getEnvironment() {
        return environment;
    }
}