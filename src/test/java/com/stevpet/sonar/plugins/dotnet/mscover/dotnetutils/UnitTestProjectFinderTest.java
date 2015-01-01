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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;


import java.util.List;

import org.junit.Test;
import org.sonar.api.utils.SonarException;
import org.sonar.test.TestUtils;

public class UnitTestProjectFinderTest {
    private UnitTestProjectFinder unitTestProjectFinder;
    private File startDirectory;
    @Test 
    public void InRoot_SolutionFound() {
        givenStart("");
        unitTestProjectFinder.gotoDirWithSolution("mySolution.sln");

    }
    
    @Test 
    public void InProject_SolutionFound() {
        givenStart("BasicControls");
        unitTestProjectFinder.gotoDirWithSolution("mySolution.sln");

    }
    
    @Test 
    public void InRoot_SolutionNotFound() {
        givenStart("");
        boolean passed=false;
        try {
            unitTestProjectFinder.gotoDirWithSolution("mySolutionBogus.sln");
        } catch(SonarException e) {
            passed=true;
        }
        assertTrue(passed);

    }
    
    @Test 
    public void InRoot_ProjectFound() {
        givenStart("");
        List<String> projects=unitTestProjectFinder.findProjectNames(".*UnitTest.*");
        assertEquals(1,projects.size());
        assertEquals("BasicControls.UnitTest",projects.get(0));

    }
    
    @Test 
    public void InRoot_TwoProjectsFound() {
        givenStart("");
        List<String> projects=unitTestProjectFinder.findProjectNames(".*BasicControls.*");
        assertEquals(2,projects.size());
        assertTrue(projects.contains("BasicControls.UnitTest"));
        assertTrue(projects.contains("BasicControls"));

    }
    
    @Test 
    public void InProject_TwoProjectsFound() {
        givenStart("BasicControls");
        List<String> projects=unitTestProjectFinder.gotoDirWithSolution("mySolution.sln").findProjectNames(".*BasicControls.*");
        assertEquals(2,projects.size());
        assertTrue(projects.contains("BasicControls.UnitTest"));
        assertTrue(projects.contains("BasicControls"));

    }
    @Test 
    public void InRoot_ProjectNotFound() {
        givenStart("");
        List<String> projects=unitTestProjectFinder.findProjectNames("garbagewillnotbefound");
        assertNotNull(projects);
        assertEquals(0,projects.size());
    }
    
    public void givenStart(String relativePath) {
        unitTestProjectFinder = new UnitTestProjectFinder();
        startDirectory=TestUtils.getResource("UnitTestProjectFinderTest/" + relativePath);
        unitTestProjectFinder.setStartDirectory(startDirectory);
    }
}
