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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ModuleSaverExecuteTest {

    
    private static final String COVERAGE_SESSION = "<CoverageSession xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
    private static final String XML_VERSION_1_0_ENCODING_UTF_8 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private OpenCoverModuleParser saver;
    private String moduleName;
    private boolean skipped;

    @Test 
    public void getModuleNameNewOpenCover() {
        String xmlDoc = getXmlDoc("skippedDueTo=\"Filter\"");
        saver=new OpenCoverModuleParser();
        saver.parse(xmlDoc);
        moduleName=saver.getModuleName();
        assertNotNull("should get name",moduleName);
    }

    @Test 
    public void isSkipped() {
        String xmlDoc = getXmlDoc("skippedDueTo=\"Filter\"");
        saver=new OpenCoverModuleParser();
        saver.parse(xmlDoc);
        skipped=saver.getSkipped();
        assertTrue("should be skipped",skipped);
    }
    
    @Test 
    public void isNotSkipped() {
        String xmlDoc = getXmlDoc("");
        saver=new OpenCoverModuleParser();
        saver.parse(xmlDoc);
        skipped=saver.getSkipped();
        assertFalse("should not be skipped",skipped);
    }
    
    @Test 
    public void skippedFollowedByNotSkipped() {
        isSkipped();
        String xmlDoc = getXmlDoc("");
        saver.parse(xmlDoc);
        skipped=saver.getSkipped();
        assertFalse("second doc should not be skipped",skipped);
    }
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
