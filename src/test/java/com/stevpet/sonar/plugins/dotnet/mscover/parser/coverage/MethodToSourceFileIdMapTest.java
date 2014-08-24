package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import java.io.File;

import org.junit.Test;
import org.junit.Assert;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.MethodToSourceFileIdMapObserver;

public class MethodToSourceFileIdMapTest {
    @Test
    public void mapper_LoadMethods_() {
        MethodToSourceFileIdMap results = loadMethodMapper();
        //38 methods, of which 3 are overloads
        Assert.assertEquals(35, results.size());
    }

    @Test
    public void parser_GetLastMethod_ShouldMatch() {
        MethodToSourceFileIdMap results = loadMethodMapper();
        results.setModuleName("tfsblame.exe");
        results.setNamespaceName("BHI.JewelSuite.Tools.TfsBlame.Properties");
        results.setClassName("Settings");
        results.setMethodName("Settings");
        String id = results.getSourceFileID();
        Assert.assertEquals("11", id);
    }
    
    @Test
    public void parser_GetFirstMethod_ShouldMatch() {
        MethodToSourceFileIdMap results = loadMethodMapper();
        results.setModuleName("unittests.dll");
        results.setNamespaceName("BHI.JewelSuite.Tools.TfsBlame.unittests");
        results.setClassName("ArgumentParserTests");
        results.setMethodName("InvocationAsExpectedShouldGetFile");
        String id = results.getSourceFileID();
        Assert.assertEquals("1", id);
    }
    
    private MethodToSourceFileIdMap loadMethodMapper() {
        XmlParserSubject parserSubject = new CoverageParserSubject();
        File file = TestUtils.getResource("coverage.xml");
        MethodToSourceFileIdMap results = new MethodToSourceFileIdMap();
        MethodToSourceFileIdMapObserver methodObserver = new MethodToSourceFileIdMapObserver();
        methodObserver.setRegistry(results);
        parserSubject.registerObserver(methodObserver);
        parserSubject.parseFile(file);
        return results;
    }
}
