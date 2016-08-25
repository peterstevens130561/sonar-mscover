package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser;

import java.io.File;
import java.util.List;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestCoverageParserSubject;

/**
 * parses VsTest generated coverage files
 * 
 * @see FilteringCoverageParser
 * 
 */
public class VsTestFilteringCoverageParser implements FilteringCoverageParser {

    private List<String> modules;

    @Override
    public FilteringCoverageParser setModulesToParse(List<String> modules) {
        this.modules = modules;
        return this;
    }

    @Override
    public void parse(SonarCoverage registry, File file) {
        VsTestModuleNameObserver moduleNameObserver = new VsTestModuleNameObserver();
        moduleNameObserver.setModulesToParse(modules);
        XmlParserSubject parserSubject = new VsTestCoverageParserSubject();

        VsTestCoverageObserver[] observers = { new VsTestFileNamesObserver(),
                new VsTestLinesObserver(), moduleNameObserver };

        for (VsTestCoverageObserver observer : observers) {
            observer.setVsTestRegistry(registry);
            parserSubject.registerObserver(observer);
        }
        parserSubject.parseFile(file);
    }

}
