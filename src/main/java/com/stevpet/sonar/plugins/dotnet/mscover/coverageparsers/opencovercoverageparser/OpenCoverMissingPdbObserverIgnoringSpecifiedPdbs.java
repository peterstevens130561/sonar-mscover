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

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs;
import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs extends OpenCoverObserver{
    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs.class);
    private boolean isMissing=false;
    private Collection<String> pdbs = new ArrayList<String>();
    
    @Override
    public void setRegistry(ProjectCoverageRepository registry) {
        // empty, as the registry is not needed
    }
    public void setPdbsThatCanBeIgnoredIfMissing(Collection<String> missingPdbsThatCanBeIgnored) {
        if(missingPdbsThatCanBeIgnored == null) {
            throw new IllegalArgumentException("pdbs can't be null");
        }
        this.pdbs = missingPdbsThatCanBeIgnored;
    }
    
    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
        registrar.inPath("Modules").inElement("Module").onAttribute("skippedDueTo", (value ->isMissing="MissingPdb".equals(value)));
        registrar.inPath("Modules/Module").inElement("ModulePath", i -> { i.withEventArgs(this::fullName);});
    }

    public void fullName(ParserEventArgs eventArgs) {
        String value=eventArgs.getValue();
        if(isMissing) {
            String msg="Missing PDB file for " + value + "\n did you use a file reference instead of a project reference ?";
            int lastSlash=value.lastIndexOf("\\");
            String name;
            if(lastSlash!=-1) {
                name =value.substring(lastSlash+1);
            } else {
                name=value;
            }
            if(pdbs.contains(name)) {
                LOG.warn(msg);
            } else {
                LOG.error(msg);
                eventArgs.setError();
            }
        }
        isMissing=false;
    }




}
