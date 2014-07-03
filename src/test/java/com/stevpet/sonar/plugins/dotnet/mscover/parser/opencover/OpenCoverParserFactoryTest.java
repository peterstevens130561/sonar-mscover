package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import java.io.File;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ConcreteParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;

public class OpenCoverParserFactoryTest {
    @Test
    public void createOpenCoverParser_ShouldSimplyParse() {
        ParserFactory parserFactory = new ConcreteParserFactory();
        SonarCoverage registry = new SonarCoverage();
        ParserSubject parser = parserFactory.createOpenCoverParser(registry);
        File file = TestUtils.getResource("coverage-report.xml");
        parser.parseFile(file);
        OpenCoverObserversTest.checkCoverageParsingResults(registry);
    }
}
