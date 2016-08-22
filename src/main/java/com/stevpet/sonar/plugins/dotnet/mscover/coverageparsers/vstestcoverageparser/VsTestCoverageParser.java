package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser;

import java.io.File;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestCoverageParserSubject;

public class VsTestCoverageParser implements CoverageParser {
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.CoverageParser
     * #parser
     * (com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage,
     * java.io.File)
     */
    @Override
    public void parse(SonarCoverage registry, File file) {
        XmlParserSubject parserSubject = new VsTestCoverageParserSubject();

        VsTestCoverageObserver[] observers = { new FileNamesObserver(),
                new VsTestLinesObserver() };

        for (VsTestCoverageObserver observer : observers) {
            observer.setVsTestRegistry(registry);
            parserSubject.registerObserver(observer);
        }
        parserSubject.parseFile(file);
    }
}
