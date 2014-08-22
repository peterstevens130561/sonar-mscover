package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubjectErrorException;

public class OpenCoverMissingPdbObserverTest {

    private OpenCoverMissingPdbObserver observer;
    @Test(expected=ParserSubjectErrorException.class)
    public void reportWithTwoMissingPdbs_ExpectExepction() {
        //Arrange
        ParserSubject parser = initializeParser();
        File file = TestUtils.getResource("coverage-report-missingPdbs.xml");
        //Act
        parser.parseFile(file);
    }
    
    @Test
    public void reportWithNoPdbsMissing_ExpectNone(){
        //Arrange
        ParserSubject parser = initializeParser();
        File file = TestUtils.getResource("coverage-report.xml");
        //Act
        parser.parseFile(file);
        //no exception expected
    }
    
    private ParserSubject initializeParser() {
        ParserSubject parser = new OpenCoverParserSubject();
        observer = new OpenCoverMissingPdbObserver();
        observer.setRegistry(null);
        parser.registerObserver(observer);
        return parser;
    }

}