package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import java.io.File;
import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;


/**
 * Parses an opencover created coverage file
 */
public class OpenCoverCoverageParser implements CoverageParser {
    private MsCoverProperties msCoverProperties;

    @SuppressWarnings("ucd")
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
