/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs;
import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class OpenCoverMissingPdbObserver extends OpenCoverObserver{
    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverMissingPdbObserver.class);
    private boolean isMissing=false;

    
    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
        registrar.inPath("Modules")
            .inPath("Module").inElement("ModulePath", r->{r.withEventArgs(this::fullName);});
        registrar.inPath("Modules")
            .inElement("Module")
                .onAttribute("skippedDueTo", this::fileRefMatcher);
                
    }

    public void fileRefMatcher(String attributeValue) {
        isMissing="MissingPdb".equals(attributeValue);
    }
    
    public void fullName(ParserEventArgs eventArgs) {
        if(isMissing) {
            LOG.error("Missing PDB file for " + eventArgs.getValue() + "\n did you use a file reference instead of a project reference ?");
            eventArgs.setError();
        }
        isMissing=false;
    }

    @Override
    public void setRegistry(ProjectCoverageRepository registry) {
        // Registry not used
    }


}
