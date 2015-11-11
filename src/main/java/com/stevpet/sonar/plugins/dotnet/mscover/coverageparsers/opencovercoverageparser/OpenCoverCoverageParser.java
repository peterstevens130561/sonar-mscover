package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import java.io.File;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;


/**
 * Parses an opencover created coverage file
 */
public class OpenCoverCoverageParser implements CoverageParser {
    private final Logger LOG = LoggerFactory.getLogger(OpenCoverCoverageParser.class);
    private MsCoverConfiguration msCoverProperties;

    @SuppressWarnings("ucd")
    public OpenCoverCoverageParser(MsCoverConfiguration msCoverProperties) {
        this.msCoverProperties = msCoverProperties;
    }

    @Override
    public void parse(SonarCoverage registry,File file) {
        if(file==null) {
            throw new IllegalArgumentException("file");
        }
        if(registry==null) {
            throw new IllegalArgumentException("registry");
        }
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
        try { 
        parser.parseFile(file);
        } catch (Exception e ) {
            LOG.error("{} thrown when parsing file {} ",e.getClass().getName(),file.getAbsolutePath());
            throw(e);
    }
        
    }
}
