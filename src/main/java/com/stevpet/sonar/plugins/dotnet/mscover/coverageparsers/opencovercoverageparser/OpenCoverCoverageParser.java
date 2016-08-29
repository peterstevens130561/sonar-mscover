package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import java.io.File;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;


/**
 * Parses an opencover created coverage file
 */
public class OpenCoverCoverageParser implements CoverageParser {
    private final Logger LOG = LoggerFactory.getLogger(OpenCoverCoverageParser.class);
    private MsCoverConfiguration msCoverProperties;
	protected XmlParserSubject parser;
	private OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs missingPdbObserver;
	private OpenCoverObserver [] observers;

    @SuppressWarnings("ucd")
    public OpenCoverCoverageParser(MsCoverConfiguration msCoverProperties) {
        this.msCoverProperties = msCoverProperties;
        parser = new XmlParserSubject();
        missingPdbObserver = new OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs();
        observers = new OpenCoverObserver[] { 
                new OpenCoverSourceFileNamesObserver(),
                new OpenCoverSequencePointsObserver(),
                missingPdbObserver};
        for(OpenCoverObserver observer: observers) {
            parser.registerObserver(observer);
        }
    }

    @Override
    public void parse(SonarCoverage registry,File file) {
        if(file==null) {
            throw new IllegalArgumentException("file");
        }
        if(registry==null) {
            throw new IllegalArgumentException("registry");
        }

        Collection<String> pdbsThatCanBeIgnoredWhenMissing = msCoverProperties.getPdbsThatMayBeIgnoredWhenMissing(); 
        missingPdbObserver.setPdbsThatCanBeIgnoredIfMissing(pdbsThatCanBeIgnoredWhenMissing);
        for(OpenCoverObserver observer: observers) {
            observer.setRegistry(registry);
        }
        try { 
        parser.parseFile(file);
        } catch (Exception e ) {
            LOG.error("{} thrown when parsing file {} ",e.getClass().getName(),file.getAbsolutePath());
            throw(e);
    }
        
    }
}
