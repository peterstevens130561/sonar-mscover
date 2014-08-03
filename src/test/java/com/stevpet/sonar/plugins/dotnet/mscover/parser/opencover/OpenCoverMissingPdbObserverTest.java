package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;

public class OpenCoverMissingPdbObserverTest {

    private OpenCoverMissingPdbObserver observer;
    @Test
    public void reportWithTwoMissingPdbs_ExpectTwo() {
        //Arrange
        ParserSubject parser = initializeParser();
        File file = TestUtils.getResource("coverage-report-missingPdbs.xml");
        //Act
        parser.parseFile(file);
        //Assert
        List<String> missingPdbs ;
        missingPdbs=observer.getMissingPdbs();
        assertNotNull(missingPdbs);
        assertEquals(2,missingPdbs.size());
        assertEquals("c:\\Development\\Jewel.Release.Oahu\\JewelEarth\\Core\\JOA Jewel Suite\\JewelExplorer.UnitTest\\bin\\Debug\\joaJewelUtilitiesUI.dll",missingPdbs.get(0));
        assertEquals("c:\\Development\\Jewel.Release.Oahu\\JewelEarth\\Core\\JOA Jewel Suite\\joaJewelUtilities.UnitTest\\bin\\Debug\\joaTouchEngine.dll",missingPdbs.get(1));
    }
    
    @Test
    public void reportWithNoPdbsMissing_ExpectNone(){
        //Arrange
        ParserSubject parser = initializeParser();
        File file = TestUtils.getResource("coverage-report.xml");
        //Act
        parser.parseFile(file);
        //Assert
        List<String> missingPdbs ;
        missingPdbs=observer.getMissingPdbs();
        assertNotNull(missingPdbs);
        assertEquals(0,missingPdbs.size());
    }
    
    private ParserSubject initializeParser() {
        ParserSubject parser = new OpenCoverParserSubject();
        observer = new OpenCoverMissingPdbObserver();
        observer.setRegistry(null);
        parser.registerObserver(observer);
        return parser;
    }

}
