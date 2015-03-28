/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers;

import java.util.ArrayList;
import java.util.Collection;

import org.picocontainer.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;

public class OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs extends OpenCoverObserver{
    
    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs.class);
    private boolean isMissing=false;
    private Collection<String> pdbs = new ArrayList<String>();
    
    public OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs(MsCoverProperties msCoverProperties) {
        this();
        pdbs= msCoverProperties.getPdbsThatMayBeIgnoredWhenMissing();
    }
    
    public OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs() {
        setPattern("Modules/Module" +
                 "|Modules/Module/FullName" +
                 "|Modules/Module/ModuleName"
                 );
    }

    public void setPdbsThatCanBeIgnoredIfMissing(Collection<String> missingPdbsThatCanBeIgnored) {
        if(missingPdbsThatCanBeIgnored == null) {
            throw new IllegalArgumentException("pdbs can't be null");
        }
        this.pdbs = missingPdbsThatCanBeIgnored;
    }
    
    @AttributeMatcher(attributeName="skippedDueTo",elementName="Module")
    public void fileRefMatcher(String attributeValue) {
        isMissing="MissingPdb".equals(attributeValue);
    }
    
    @ElementMatcher(elementName="FullName")
    public void fullName(String value) {
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
                super.setError();
            }
        }
        isMissing=false;
    }
}
