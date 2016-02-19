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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.WildcardPattern;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.NoAssemblyDefinedMsCoverException;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.SolutionHasNoProjectsSonarException;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

public abstract class AbstractAssembliesFinder implements AssembliesFinder {

    private static Logger LOG = LoggerFactory.getLogger(AbstractAssembliesFinder.class);

    private WildcardPattern[] inclusionMatchers;
    private List<String> assemblies;
    protected MsCoverConfiguration propertiesHelper;

    private Pattern testProjectPattern = Pattern.compile(".*");

    public AbstractAssembliesFinder(MsCoverConfiguration propertiesHelper) {
        this.propertiesHelper=propertiesHelper;
    }

    public String findUnitTestAssembliesDir(VisualStudioSolution solution) {
        File solutionDirectory=solution.getSolutionDir();
        List<VisualStudioProject> projects=solution.getProjects();
        List<String> assemblies = findUnitTestAssembliesFromConfig(solutionDirectory,projects);
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
     */
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
        if(projects==null || projects.isEmpty()) {
            throw new SolutionHasNoProjectsSonarException() ;
        }
        assemblies=new ArrayList<String>();
        for(VisualStudioProject project: projects) {
            if(project.isUnitTest() && matchesPattern(project)) {
                addUnitTestAssembly(project);
            }
        }
        return assemblies;       
    }

    private boolean matchesPattern(VisualStudioProject project) {
        Preconditions.checkState(testProjectPattern!=null,"testProjectPattern");
        String name=project.getAssemblyName();
        Matcher matcher=testProjectPattern.matcher(name);
        return matcher.matches();
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
        File assemblyFile=project.getArtifactFile();
        if(assemblyFile==null) {
            throw new NoAssemblyDefinedMsCoverException(buildConfiguration,buildPlatform);
        }

        if(!assemblyFile.exists() ) {
            assemblyFile=searchNonExistingFile(assemblyFile,project,buildConfiguration);            
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
    
     public AssembliesFinder setTestProjectPattern(@Nonnull Pattern pattern) {
        this.testProjectPattern=pattern;
        return this;
    }

}