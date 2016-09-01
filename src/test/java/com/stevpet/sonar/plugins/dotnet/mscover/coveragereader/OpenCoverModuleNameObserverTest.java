package com.stevpet.sonar.plugins.dotnet.mscover.coveragereader;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.common.parser.ParserData;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.ModuleNameObserver;

public class OpenCoverModuleNameObserverTest {

    private ModuleNameObserver observer = new OpenCoverModuleNameObserver();
    @Mock private ParserData parserData;
    private XmlParser parser;
    private File file;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        file = TestUtils.getResource("observers/OpenCoverModuleNameObserver.xml");
        assertNotNull("file not found (mvn clean install)",file);
        parser = new DefaultXmlParser(parserData);
        parser.registerObserver(observer);
    }

    @Test
    public void noModuleSpecified_ShouldParseAllModules() {
        parser.parseFile(file);
        verify(parserData,times(0)).setSkipThisLevel();
    }
    
    @Test
    public void nullModuleSpecified_ShouldParseAllModules() {
        List<String> modules = null;
        observer.addModulesToParse(modules);
        parser.parseFile(file);
        
        verify(parserData,times(0)).setSkipThisLevel(); 
    }
    
    @Test
    public void oneModuleSpecified_ShouldParseThatModule() {
        List<String> modules = new ArrayList<String>();
        modules.add("BHI.JewelEarth.ThinClient.Service.dll");
        observer.addModulesToParse(modules);
        parser.parseFile(file);
          
        verify(parserData,times(0)).setSkipThisLevel();     
    }
    
    @Test
    public void oneModuleSpecified_ShouldNotParseOtherModule() {
        List<String> modules = new ArrayList<String>();
        modules.add("donotparseme");
        observer.addModulesToParse(modules);
        parser.parseFile(file);
        
        verify(parserData,times(1)).setSkipThisLevel();     
    }
    
}
