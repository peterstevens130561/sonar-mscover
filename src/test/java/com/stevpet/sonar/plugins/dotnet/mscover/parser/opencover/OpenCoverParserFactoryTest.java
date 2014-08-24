package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import java.io.File;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.ConcreteOpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.ConcreteParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.ParserFactory;

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
}
