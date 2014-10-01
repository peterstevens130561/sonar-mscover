package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.ConcreteOpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.ParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.ConcreteVsTestParserFactory;

public class OpenCoverParserFactoryTest {
    @Test
    public void createOpenCoverParser_ShouldSimplyParse() {
        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        SonarCoverage registry = new SonarCoverage();
        XmlParserSubject parser = parserFactory.createOpenCoverParser(registry);
        File file = TestUtils.getResource("coverage-report.xml");
        parser.parseFile(file);
        OpenCoverObserversTest.checkCoverageParsingResults(registry);
    }
    
    
    @Test
    public void createOpenCoverParser_ShouldHaveThreeObservers() {
        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        SonarCoverage registry = new SonarCoverage();
        XmlParserSubject parser = parserFactory.createOpenCoverParser(registry);
        List<ParserObserver> observers=parser.getObservers();
        assertEquals(3,observers.size());
        
    }
    
    @Test
    public void createOpenCoverParserWithoutMissingPdb_ShouldHaveNormalObservers() {
        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        SonarCoverage registry = new SonarCoverage();
        XmlParserSubject parser = parserFactory.createOpenCoverParser(registry);
        List<ParserObserver> observers=parser.getObservers();
        assertEquals(3,observers.size());
        boolean found=false;
        for(ParserObserver observer : observers) {
            if ( observer instanceof OpenCoverMissingPdbObserver ) {
                found=true;
            }
        }
        assertTrue(found);
    }
    
    @Test
    public void createOpenCoverParserWithMissingPdb_ShouldHaveNewObservers() {
        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        SonarCoverage registry = new SonarCoverage();
        List<String> missingPdbsThatCanBeIgnored = new ArrayList<String>();
        XmlParserSubject parser = parserFactory.createOpenCoverParser(registry,missingPdbsThatCanBeIgnored);
        List<ParserObserver> observers=parser.getObservers();
        assertEquals(3,observers.size());
        boolean foundOrig=checkFound(observers,OpenCoverMissingPdbObserver.class);
        boolean foundNew=checkFound(observers,OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs.class);
        assertFalse(foundOrig);
        assertTrue(foundNew);
    }
    
    
    public boolean checkFound(List<ParserObserver> observers,Class clazz) {
        boolean found=false;
        for(ParserObserver observer : observers) {
            if ( observer.getClass()== clazz ) {
                found=true;
            }
        }  
        return found;
    }
 
    
}
