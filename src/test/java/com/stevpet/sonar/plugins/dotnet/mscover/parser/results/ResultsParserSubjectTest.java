package com.stevpet.sonar.plugins.dotnet.mscover.parser.results;


import java.io.File;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;


import org.junit.Assert;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;

public class ResultsParserSubjectTest {

    @Test
    public void createParser_ShouldWork() {
        ParserSubject parserSubject = new ResultsParserSubject();
        Assert.assertNotNull(parserSubject);
    }
    
    @Test
    public void createParser_ParseResultsFile() throws FactoryConfigurationError, XMLStreamException {
        ParserSubject parserSubject = new ResultsParserSubject();
        File file = TestUtils.getResource("results.trx");
        parserSubject.parseFile(file);
    }
}
