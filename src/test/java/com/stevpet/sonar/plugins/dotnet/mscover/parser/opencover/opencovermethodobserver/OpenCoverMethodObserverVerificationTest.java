package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover.opencovermethodobserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
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
        XmlParserSubject parser = new XmlParserSubject();
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
