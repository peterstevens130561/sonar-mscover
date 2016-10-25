/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import org.junit.Test;


public class ResolveOtherFsTest {

    @Test
    public void solutionInRoot() {
        File root = new File("c:/Development/Radiant/Main");
        File solution = new File("c:/Development/Radiant/Main/JewelEarth/Solution");
        String path = getPathFromRootToSolution(solution, root);
        assertEquals("JewelEarth\\Solution", path);
    }

    @Test
    public void solutionNotInRoot() {
        File root = new File("c:/Development/Radiant/Bogus");
        File solution = new File("c:/Development/Radiant/Main/JewelEarth/Solution");
        try{ 
            getPathFromRootToSolution(solution, root);
        }catch(IllegalStateException e) {
                return;
            }
        fail("expect exception");
    }
        
    @Test
    public void rootBelowSolution() {
        File root = new File("c:/Development/Radiant/Main/JewelEarth/Solution/Fun");
        File solution = new File("c:/Development/Radiant/Main/JewelEarth/Solution"); 
        try{ 
            getPathFromRootToSolution(solution, root);
        }catch(IllegalStateException e) {
                return;
            }
        fail("expect exception");
    }
    
    @Test
    public void InSameFs_pass() {
        File root = new File("c:/Development/Radiant/Main");
        File toResolve = new File("c:/build/bogus/Radiant/JewelEarth/Solution/Project/File.cs");
        File baseDir = new File("c:/Development/Radiant/Main/JewelEarth/Solution");

        File resolved = resolve(toResolve, baseDir, root);
        File expected = new File(baseDir, "Project/File.cs");
        assertEquals("expect to be found", expected, resolved);
    }

    @Test
    public void InOtherFs_fail() {
        File root = new File("c:/Development/Radiant/Main");
        File toResolve = new File("c:/build/bogus/Radiant/JewelEarth/Solution/Project/File.cs");
        File baseDir = new File("c:/Development/Radiant/Main/JewelEarth/Core");

        File resolved = resolve(toResolve, baseDir, root);
        File expected = null;
        assertEquals("expect to be not found", expected, resolved);
    }

    @Test
    public void InSameFsAmbiguous_pass() {
        File root = new File("c:/Development/Radiant/Main");
        File toResolve = new File("c:/build/bogus/Radiant/JewelEarth/Solution/Solution/Project/File.cs");
        File baseDir = new File("c:/Development/Radiant/Main/JewelEarth/Solution");

        File resolved = resolve(toResolve, baseDir, root);
        File expected = new File(baseDir, "Solution/Project/File.cs");
        assertEquals("expect to be found", expected, resolved);
    }

    private String getPathFromRootToSolution(File solutionDir, File rootDir) {
        String rootPath = null;
        String solutionPath = null;
        try {
            rootPath = rootDir.getCanonicalPath();
            solutionPath = solutionDir.getCanonicalPath();
        } catch (IOException e) {
            String msg = "Could not get canonical path " + e.getLocalizedMessage();
            throw new IllegalStateException(msg, e);
        }
        if (!solutionPath.startsWith(rootPath)) {
            String msg = "Project=" + solutionPath + " not found in root= " + rootPath;
            throw new IllegalStateException(msg);
        }
        int len = rootPath.length();
        String pathToSolution = solutionPath.substring(len + 1);
        return pathToSolution;
    }

    private File resolve(File resolveFile, File baseDir, File rootDir) {
       String solutionPath = getPathFromRootToSolution(baseDir,rootDir);
       String resolvePath = getCanonicalPath(resolveFile);
       int firstIndex=resolvePath.indexOf(solutionPath);
       if(firstIndex==-1) {
           return null; // not in solution
       }
       int endOfSolutionPath=firstIndex+solutionPath.length();

       String relativePath=resolvePath.substring(endOfSolutionPath);
       return new File(baseDir,relativePath);
    }
    
    
    private String getCanonicalPath(File file)  {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            String msg ="Could not get CanonicalPath for " + file.getAbsolutePath();
            throw new IllegalStateException(msg);
        }
    }
}
