/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import java.io.File;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;


/**
 * Parses an opencover created coverage file
 */
public class OpenCoverCoverageParser implements CoverageParser {
    private final Logger LOG = LoggerFactory.getLogger(OpenCoverCoverageParser.class);
    private MsCoverConfiguration msCoverProperties;
	protected XmlParser parser;
	private OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs missingPdbObserver;
	private OpenCoverObserver [] observers;

    @SuppressWarnings("ucd")
    public OpenCoverCoverageParser(MsCoverConfiguration msCoverProperties) {
        this.msCoverProperties = msCoverProperties;
        parser = new DefaultXmlParser();
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
    public void parse(ProjectCoverageRepository registry,File file) {
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
