package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.exceptions.ParserSubjectErrorException;

public class OpenCoverMissingPdbObserverIgnoringSpecifiedPdbsTest {
    private OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs observer;
    private List<String> pdbs = new ArrayList<String>();
    @Test(expected=ParserSubjectErrorException.class)
    public void reportWithTwoMissingPdbs_ExpectExepction() {
        //Arrange
        XmlParserSubject parser = initializeParser();
        File file = TestUtils.getResource("coverage-report-missingPdbs.xml");
        //Act
        parser.parseFile(file);
    }
  
    @Test(expected=ParserSubjectErrorException.class)
    public void reportWithTwoMissingPdbs_IgnoreBothExpectException() {
        //Arrange
        XmlParserSubject parser = initializeParser();
        pdbs.add("joaJewelUtilitiesUI.dll");
        File file = TestUtils.getResource("coverage-report-missingPdbs.xml");
        //Act
        parser.parseFile(file);
    }
   
    @Test
    public void reportWithTwoMissingPdbs_IgnoreBothExpectNoException() {
        //Arrange
        XmlParserSubject parser = initializeParser();
        pdbs.add("joaJewelUtilitiesUI.dll");
        pdbs.add("joaTouchEngine.dll");
        File file = TestUtils.getResource("coverage-report-missingPdbs.xml");
        //Act
        parser.parseFile(file);
    }
    @Test
    public void reportWithNoPdbsMissing_ExpectNone(){
        //Arrange
        XmlParserSubject parser = initializeParser();
        File file = TestUtils.getResource("coverage-report.xml");
        //Act
        parser.parseFile(file);
        //no exception expected
    }
    
    private XmlParserSubject initializeParser() {
        XmlParserSubject parser = new OpenCoverParserSubject();
        observer = new OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs();
        observer.setPdbsThatCanBeIgnoredIfMissing(pdbs);
        observer.setRegistry(null);
        parser.registerObserver(observer);
        return parser;
    }

}
