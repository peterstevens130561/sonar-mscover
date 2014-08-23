package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover.opencovermethodobserver;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverMethodObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserSubject;
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
        OpenCoverParserSubject parser = new OpenCoverParserSubject();
        parser.registerObserver(observer);
        File file=TestUtils.getResource("coverage-report.xml");
        parser.parseFile(file);
        assertEquals(141,registry.size());
    }
}
