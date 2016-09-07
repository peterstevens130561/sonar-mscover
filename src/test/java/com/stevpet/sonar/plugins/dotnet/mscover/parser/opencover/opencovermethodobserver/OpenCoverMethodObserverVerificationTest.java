/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover.opencovermethodobserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverMethodObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

public class OpenCoverMethodObserverVerificationTest {
	 
	
    private static final String METHOD_NAME = "RegisterHandler";
	private static final String CLASS_NAME = "OperationHandlerSet";
	private static final String BHI_JEWEL_EARTH_THIN_CLIENT_SERVICE = "BHI.JewelEarth.ThinClient.Service";
	private static final String NAMESPACE_NAME = BHI_JEWEL_EARTH_THIN_CLIENT_SERVICE;
	private static final String MODULE_NAME = "BHI.JewelEarth.ThinClient.Service.dll";
	private MethodToSourceFileIdMap mockRegistry ;
	private OpenCoverMethodObserver observer;
	
    @Before
    public void before() {
    	observer = new OpenCoverMethodObserver();
    	MethodToSourceFileIdMap registry = new MethodToSourceFileIdMap();
        observer.setRegistry(registry);
		mockRegistry = mock(MethodToSourceFileIdMap.class);    
        observer.setRegistry(mockRegistry);
        XmlParser parser = new DefaultXmlParser();
        parser.registerObserver(observer);
        File file=TestUtils.getResource("OpenCoverMethodObserver.xml"); 
        parser.parseFile(file);
    }
    
    @Test
    public void CheckParserModuleName() {
        assertEquals("ModuleName",MODULE_NAME,observer.getModuleName());
    }
    
    @Test
    public void CheckParserNameSpaceName() {
        assertEquals("NameSpaceName",NAMESPACE_NAME,observer.getNameSpaceName());
    }
    
    @Test
    public void CheckParserClassName() {
        assertEquals("ClassName",CLASS_NAME,observer.getClassName());
    }

    @Test
    public void CheckParserMethodName() {
        assertEquals("MethodName",METHOD_NAME,observer.getMethodName());     
    }

    @Test
    public void CheckParserSubmitMethodID() {
        MethodId methodId = new MethodId(MODULE_NAME, NAMESPACE_NAME, CLASS_NAME, METHOD_NAME);
		verify(mockRegistry,times(1)).add(methodId, "84");
    }
    
}
