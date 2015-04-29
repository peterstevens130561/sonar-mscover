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
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageReaderStep;

/**
 * Parses an opencover created coverage file
 */
public class OpenCoverCoverageReader implements CoverageReaderStep {
	private final static Logger LOG = LoggerFactory.getLogger(OpenCoverCoverageReader.class);
    private MsCoverProperties msCoverProperties;

    public OpenCoverCoverageReader(MsCoverProperties msCoverProperties) {
        this.msCoverProperties = msCoverProperties;
    }

    @Override
    public void read(SonarCoverage registry,File file) {
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
