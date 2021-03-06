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
package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;


import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;

public class OpenCoverFullNameObserverTest {
    private final OpenCoverFullNameObserver observer = new OpenCoverFullNameObserver();
    private  DefaultXmlParser parser;
    
    @Before
    public void before() {
        parser = new DefaultXmlParser();
        parser.registerObserver(observer);      
    }
    
    @Test
    public void getModuleName() {
        parser.parseString(getXmlDoc(""));
        assertEquals("should be mscorlib","mscorlib",observer.getModuleName());
    }
    
    @Test
    public void getSkippedTrue() {
        parser.parseString(getXmlDoc("skippedDueTo=\"Filter\""));
        assertTrue("if attribute is present, then skipped should be true",observer.getSkipped());
    }
    
    @Test
    public void getSkippedFalse() {
        parser.parseString(getXmlDoc(""));
        assertFalse("if attribute is absent, then skipped should be false",observer.getSkipped());
    }
    
    
    private static final String COVERAGE_SESSION = "<CoverageSession xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
    private static final String XML_VERSION_1_0_ENCODING_UTF_8 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private String getXmlDoc(String attribute) {
        String xmlDoc=XML_VERSION_1_0_ENCODING_UTF_8 + 
                COVERAGE_SESSION+
                "<Modules><Module " + attribute + " hash=\"3F-86-44-9D-0D-07-6A-A0-B0-74-64-5F-BD-19-3E-CE-6E-DE-17-8C\">" +
                "<ModulePath>C:\\windows\\Microsoft.Net\\assembly\\GAC_32\\mscorlib\\v4.0_4.0.0.0__b77a5c561934e089\\mscorlib.dll</ModulePath>" +
                "<ModuleTime>2015-07-31T18:48:30Z</ModuleTime>" +
                "<ModuleName>mscorlib</ModuleName>" +
                "<Classes/>" +
                "</Module></Modules></CoverageSession>";
        return xmlDoc;
    }
}
