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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import static org.mockito.Mockito.when;
public class IgnoreMissingAssemblyResolverTest {

    private @Mock MsCoverConfiguration msCoverConfiguration;
    private AssemblyResolver assemblyResolver = new IgnoreMissingAssemblyResolver();
    private AssemblyResolverTestUtils utils = new AssemblyResolverTestUtils();

    private String fileName="unittest.dll";
    Collection<String> list;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        assemblyResolver.setMsCoverProperties(msCoverConfiguration);
        utils.setAssemblyResolver(assemblyResolver);
        utils.givenAssembly(fileName);
    }
    
    @Test
    public void resolveAssembly_PropertyNotSet_ExpectNotResolved() {      
        utils.resolveAssembly();
        utils.verifyNotResolved();
    }
  
    @Test
    public void resolveAssembly_PropertyDoesNotContain_ExpectNotResolved() {
        givenAssemblyThatCanBeIgnored("different");

        utils.resolveAssembly();       
        utils.verifyNotResolved();    
    }
    
    @Test
    public void resolveAssembly_PropertyDoesContain_ExpectNotResolved() {

        givenAssemblyThatCanBeIgnored("different");
        givenAssemblyThatCanBeIgnored(fileName); // the match
        
        utils.resolveAssembly();
        utils.verifyShouldBeIgnored();       
    }

    private void givenAssemblyThatCanBeIgnored(String name) {
        if(list==null) {
            list=new ArrayList<String>();
        }
        list.add(name);
        when(msCoverConfiguration.getUnitTestAssembliesThatCanBeIgnoredIfMissing()).thenReturn(list);
    }
    
    

}
