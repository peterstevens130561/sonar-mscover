package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;

public class OpenCoverModuleSummaryObserverTests {


    private ModuleSummaryObserver moduleSummaryObserver ;
    private XmlParser xmlParser ;
    
    @Before
    public void before() {
        moduleSummaryObserver = new OpenCoverModuleSummaryObserver();
        xmlParser = new DefaultXmlParser();
        xmlParser.registerObserver(moduleSummaryObserver); 
    }
    
    @Test
    public void notCovered() {
       File file = getTestFile("NotCovered.xml");
       xmlParser.parseFile(file);
       assertTrue("no coverage expected",moduleSummaryObserver.isNotCovered());
    }


    @Test
    public void isCovered() {
       File file = getTestFile("SequencePointsCovered.xml");
       xmlParser.parseFile(file);
       assertFalse("coverage expected",moduleSummaryObserver.isNotCovered());
    }
    
    
    private File getTestFile(String name) {
        File file= TestUtils.getResource("OpenCoverModuleSummaryObserver/" + name);
        assertNotNull(file );
        return file;
    }
}
