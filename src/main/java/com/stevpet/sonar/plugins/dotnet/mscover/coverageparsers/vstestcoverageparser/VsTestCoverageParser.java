package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser;

import java.io.File;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

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
        XmlParser xmlParser = new DefaultXmlParser();

        VsTestCoverageObserver[] observers = { new VsTestFileNamesObserver(),
                new VsTestLinesObserver() };

        for (VsTestCoverageObserver observer : observers) {
            observer.setVsTestRegistry(registry);
            xmlParser.registerObserver(observer);
        }
        xmlParser.parseFile(file);
    }
}
