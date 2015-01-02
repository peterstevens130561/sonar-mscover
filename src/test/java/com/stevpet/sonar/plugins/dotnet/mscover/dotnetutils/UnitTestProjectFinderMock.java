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
package com.stevpet.sonar.plugins.dotnet.mscover.dotnetutils;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class UnitTestProjectFinderMock extends GenericClassMock<UnitTestProjectFinder> {

    public UnitTestProjectFinderMock() {
        super(UnitTestProjectFinder.class);
        when(instance.setStartDirectory(any(File.class))).thenReturn(instance);
        when(instance.gotoDirWithSolution(anyString())).thenReturn(instance);     
    }
     
    public void givenFindUnitTestProjects(String pattern,List<String> projectNames) {
        when(instance.findProjectNames(pattern)).thenReturn(projectNames);
    }
    
    public void givenFindProjectDirectories(String pattern,
            List<File> projectDirectories) {
        when(instance.findUnitTestProjectDirectories(pattern)).thenReturn(projectDirectories);
        
    }
    public void verifyStartDirectory(File startDirectory) {
        verify(instance,times(1)).setStartDirectory(startDirectory);
    }
    
    public void verifyGotoDirWithSoluction(String solutionName) {
        verify(instance,times(1)).gotoDirWithSolution(solutionName);
    }

    public void verifyFindProjectNames(String pattern) {
        verify(instance,times(1)).findProjectNames(pattern);
    }

    public void givenFindsNoUnitTestProjects() {
        when(instance.findUnitTestProjectDirectories(anyString())).thenReturn(new ArrayList<File>());      
    }



}
