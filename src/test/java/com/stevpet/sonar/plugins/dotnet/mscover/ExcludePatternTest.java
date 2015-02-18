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
package com.stevpet.sonar.plugins.dotnet.mscover;



import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.utils.WildcardPattern;

public class ExcludePatternTest {
    
    @Test
    public void Straight_Exact_ShouldMatch() {
        String pattern = "joaBox";
        boolean result = matchFilePattern(pattern,pattern);
        Assert.assertTrue(result);
    }
    
    @Test
    public void Straight_InDirectory_ShouldNotMatch() {
        String pattern = "joaBox";
        boolean result = matchFilePattern(pattern,"mydir/" + pattern);
        Assert.assertFalse(result);
    }
    
    @Test
    public void DirectoyPrefix_InDirectory_ShouldMatch() {
        String pattern = "**/joaBox";
        boolean result = matchFilePattern(pattern,"mydir/joaBox");
        Assert.assertTrue(result);
    }
    
    @Test
    public void DirectoyPrefix_InDeepDirectory_ShouldMatch() {
        String pattern = "**/joaBox";
        boolean result = matchFilePattern(pattern,"mydir/deep/joaBox");
        Assert.assertTrue(result);
    }
   
    
    @Test
    public void Directory_InDeepDirectory_ShouldMatch() {
        String pattern = "joaGeometries/**";
        boolean result = matchFilePattern(pattern,"joaGeometries/myfile.cpp");
        Assert.assertTrue(result);
    }
    
    @Test
    public void Directory_InTwoLevelDeepDirectory_ShouldMatch() {
        String pattern = "joaGeometries/**";
        boolean result = matchFilePattern(pattern,"joaGeometries/c/myfile.cpp");
        Assert.assertTrue(result);
    }
    @Test
    public void DirectorySimple_InDeepDirectory_ShouldMatch() {
        String pattern = "joaGeometries";
        boolean result = matchFilePattern(pattern,"joaGeometries/myfile.cpp");
        Assert.assertFalse(result);
    }
    
    @Test
    public void DirectoryWithDirSep_InDeepDirectory_ShouldMatch() {
        String pattern = "joaGeometries/";
        boolean result = matchFilePattern(pattern,"joaGeometries/myfile.cpp");
        Assert.assertFalse(result);
    }
    
    @Test
    public void DirectoryWithWildcardDirSep_InDeepDirectory_ShouldMatch() {
        String pattern = "joa*/";
        boolean result = matchFilePattern(pattern,"joaGeometries/myfile.cpp");
        Assert.assertFalse(result);
    }
    
    @Test
    public void MultipleFiles_MatchMiddle_ShouldMatch() {
        String pattern = "joa,job,joc";
        boolean result = matchFilePatterns(pattern,"job");
        Assert.assertTrue(result);
    }
    
    @Test
    public void MultipleFiles_NoMatch_ShouldNotMatch() {
        String pattern = "joa,job,joc";
        
        boolean result = matchFilePatterns(pattern,"jod");
        Assert.assertFalse(result);
    }
    
    @Test
    public void MultipleFilesWildcard_Match_ShouldMatch() {
        String pattern = "joa,kkl,ko?";
        
        boolean result = matchFilePatterns(pattern,"kot");
        Assert.assertTrue(result);
    }
    public boolean matchFilePattern(String antPattern,String key) {
        WildcardPattern matcher = WildcardPattern.create(antPattern, "/");
        return matcher.match(key);
      }
    
    public boolean matchFilePatterns(String antPatterns,String key) {
        String[] patterns = antPatterns.split(",");
        WildcardPattern[] matchers = WildcardPattern.create(patterns);
        return WildcardPattern.match(matchers,key);
      }

}
