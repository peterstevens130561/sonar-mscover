package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser;

public class DefaultTestResultsParserTest {

    private UnitTestRegistry repository = new UnitTestRegistry();
    private final TestResultsParser parser = new DefaultTestResultsParser(repository) ;
    
    @Test
    public void importTest() {
        File file = new File("C:/temp/testresults.trx");
        assertNotNull(file);
        parser.parse(file);
    }
}
