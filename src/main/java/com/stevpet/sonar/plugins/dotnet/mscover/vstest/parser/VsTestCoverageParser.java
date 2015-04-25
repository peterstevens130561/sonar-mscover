package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageParserStep;

public class VsTestCoverageParser implements CoverageParserStep {
	private final static Logger LOG = LoggerFactory.getLogger(OpenCoverCoverageParser.class);
    private MsCoverProperties msCoverProperties;

    public VsTestCoverageParser(MsCoverProperties msCoverProperties) {
        this.msCoverProperties = msCoverProperties;
    }
	@Override
	public void parse(SonarCoverage registry, File file) {

        XmlParserSubject parserSubject = new CoverageParserSubject();

        VsTestCoverageObserver[] observers = {

                //new VsTestSourceFileNamesToCoverageObserver(),
                new VsTestLinesObserver(),
               // new VsTestSourceFileNamesToSourceFileNamesObserver()
        };
        
        for(VsTestCoverageObserver observer : observers) {
            observer.setVsTestRegistry(registry);
            parserSubject.registerObserver(observer);            
        }
	}

}
