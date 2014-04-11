package com.stevpet.sonar.plugins.dotnet.mscover.parser.results;

import java.io.File;

import org.junit.Test;
import org.junit.Assert;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;

public class ResultsParserTest {
    @Test
    public void parser_GetCounters_ShouldMatch() {
        ParserSubject parserSubject = new ResultsParserSubject();
        File file = TestUtils.getResource("results.trx");
        ResultsObserver resultsObserver = new ResultsObserver();
        parserSubject.registerObserver(resultsObserver);
        parserSubject.parseFile(file);   
        Assert.assertEquals(186, resultsObserver.getExecutedTests());
    }

}
