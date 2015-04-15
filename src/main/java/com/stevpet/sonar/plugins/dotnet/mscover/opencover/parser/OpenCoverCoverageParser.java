package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import java.io.File;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageParserStep;

/**
 * Parses an opencover created coverage file
 */
public class OpenCoverCoverageParser implements CoverageParserStep {
	private final static Logger LOG = LoggerFactory.getLogger(OpenCoverCoverageParser.class);
    private MsCoverProperties msCoverProperties;

    public OpenCoverCoverageParser(MsCoverProperties msCoverProperties) {
        this.msCoverProperties = msCoverProperties;
    }

    @Override
    public void parse(SonarCoverage registry,File file) {
        XmlParserSubject parser = new OpenCoverParserSubject();
        Collection<String> pdbsThatCanBeIgnoredWhenMissing = msCoverProperties.getPdbsThatMayBeIgnoredWhenMissing();
        OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs  missingPdbObserver = new OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs() ;
        missingPdbObserver.setPdbsThatCanBeIgnoredIfMissing(pdbsThatCanBeIgnoredWhenMissing);
        OpenCoverObserver [] observers = { 
                new OpenCoverSourceFileNamesObserver(),
                new OpenCoverSequencePointsObserver(),
                missingPdbObserver};
        for(OpenCoverObserver observer: observers) {
            observer.setRegistry(registry);
            parser.registerObserver(observer);
        }
        parser.parseFile(file);
    }
}
