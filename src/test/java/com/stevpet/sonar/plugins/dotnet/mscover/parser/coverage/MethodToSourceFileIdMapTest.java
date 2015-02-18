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
package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Test;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIdModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestMethodToSourceFileIdMapObserver;

public class MethodToSourceFileIdMapTest {
    
    Logger LOG = LoggerFactory.getLogger(MethodToSourceFileIdMapTest.class);
    @Test
    public void addOne_GetShouldWork() {
        MethodToSourceFileIdMap results = new MethodToSourceFileIdMap() ;
        MethodIdModel methodOne = new MethodIdModel("module.dll","namespace","class","method");
        results.add(methodOne,"1");
        assertEquals(1,results.size());
        String actual=results.get(methodOne);
        assertEquals("1",actual);
    }
    
    @Test
    public void addMany_GetShouldWork() {
        MethodToSourceFileIdMap results = new MethodToSourceFileIdMap() ;
        for(int i=0;i<10;i++) {
            MethodIdModel methodOne = new MethodIdModel("module.dll","namespace","class","method");
            methodOne.setNamespaceName("namespace" + i);
            //LOG.info("i" + methodOne.hashCode() +  " " + getPos(methodOne,16));
            
            results.add(methodOne,"n" + i);
            String fileId=results.get(methodOne);
            assertEquals("n" + i,fileId);
            for(int j=0;j<10;j++) {
                methodOne=new MethodIdModel("module.dll","namespace" + j,"class","method");
                if(i<j) {
                    assertNull("" + i + " " + j,results.get(methodOne));
                } else {
                    assertEquals("" + i + " " + j,"n" + j,results.get(methodOne));
                }
            }
        }
    }
    
 
    /**
     * Returns index for hash code h.
     */
    static int indexFor(int h, int length) {
        // assert Integer.bitCount(length) == 1 : "length must be a non-zero power of 2";
        return h & (length-1);
    }
    @Test
    public void mapper_LoadMethods_() {
        MethodToSourceFileIdMap results = loadMethodMapper();
        //38 methods, of which 3 are overloads
        Assert.assertEquals(35, results.size());
    }


    
    @Test
    public void parser_GetLastMethodViaGet_ShouldMatch() {
        MethodToSourceFileIdMap results = loadMethodMapper();
        MethodIdModel method = new MethodIdModel();
        method.setModuleName("tfsblame.exe");
        method.setNamespaceName("BHI.JewelSuite.Tools.TfsBlame.Properties");
        method.setClassName("Settings");
        method.setMethodName("Settings");
        String id = results.get(method);
        Assert.assertEquals("11", id);
    }
  
    
    @Test
    public void parser_GetFirstMethodViaGet_ShouldMatch() {
        MethodToSourceFileIdMap results = loadMethodMapper();
        MethodIdModel method = new MethodIdModel();
        method.setModuleName("unittests.dll");
        method.setNamespaceName("BHI.JewelSuite.Tools.TfsBlame.unittests");
        method.setClassName("ArgumentParserTests");
        method.setMethodName("InvocationAsExpectedShouldGetFile");
        String id = results.get(method);
        Assert.assertEquals("1", id);
    }
    private MethodToSourceFileIdMap loadMethodMapper() {
        XmlParserSubject parserSubject = new CoverageParserSubject();
        File file = TestUtils.getResource("coverage.xml");
        MethodToSourceFileIdMap results = new MethodToSourceFileIdMap();
        VsTestMethodToSourceFileIdMapObserver methodObserver = new VsTestMethodToSourceFileIdMapObserver();
        methodObserver.setRegistry(results);
        parserSubject.registerObserver(methodObserver);
        parserSubject.parseFile(file);
        return results;
    }
}
