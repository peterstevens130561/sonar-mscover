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
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover.opencovermethodobserver;


import static org.junit.Assert.*;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverMethodObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

public class OpenCoverMethodObserverTest {

	private OpenCoverMethodObserver observer;
    private MethodToSourceFileIdMap registry;
    @Before
    public void before() {
        observer = new OpenCoverMethodObserver();
        registry = new MethodToSourceFileIdMap();
        observer.setRegistry(registry);
    }
    
    @Test
    public void SimpleMethod_InRegistry() {

        observer.setRegistry(registry);
        observer.setModuleName("C:\\development\\aap.noot.mies.dll");
        observer.setNamespaceAndClassName("aap.noot.mies.john");
        observer.setMethodName("void aap.noot.mies.john::myfun()");
        observer.setFileId("10");
        assertEquals(1,registry.size());
    }
    
    @Test
    public void ParseFile() {
        observer.setRegistry(registry);
        XmlParser parser = new DefaultXmlParser();
        parser.registerObserver(observer);
        File file=TestUtils.getResource("coverage-report.xml");
        parser.parseFile(file);
        assertEquals(127,registry.size());
        assertEquals(29,registry.getDuplicates());
    }
    
    @Test
    public void ParseFileCheckCount() {
    	MethodToSourceFileIdMap mockRegistry = mock(MethodToSourceFileIdMap.class);
    	
        observer.setRegistry(mockRegistry);
        XmlParser parser = new DefaultXmlParser();
        parser.registerObserver(observer);
        File file=TestUtils.getResource("coverage-report.xml");
        parser.parseFile(file);
        verify(mockRegistry,times(170)).add(any(MethodId.class), anyString());
    }
    
   
}
