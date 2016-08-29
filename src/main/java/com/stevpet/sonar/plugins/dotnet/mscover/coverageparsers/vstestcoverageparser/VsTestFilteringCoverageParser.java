package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser;

import java.io.File;
import java.util.List;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

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
        XmlParser xmlParser = new DefaultXmlParser();

        VsTestCoverageObserver[] observers = { new VsTestFileNamesObserver(),
                new VsTestLinesObserver(), moduleNameObserver };

        for (VsTestCoverageObserver observer : observers) {
            observer.setVsTestRegistry(registry);
            xmlParser.registerObserver(observer);
        }
        xmlParser.parseFile(file);
    }

}
