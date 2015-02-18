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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;

public class OpenCoverMissingPdbObserver extends OpenCoverObserver{
    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverMissingPdbObserver.class);
    private boolean isMissing=false;

    
    public OpenCoverMissingPdbObserver() {
        setPattern("Modules/Module" +
                 "|Modules/Module/FullName" +
                 "|Modules/Module/ModuleName"
                 );
    }

    @AttributeMatcher(attributeName="skippedDueTo",elementName="Module")
    public void fileRefMatcher(String attributeValue) {
        isMissing="MissingPdb".equals(attributeValue);
    }
    
    @ElementMatcher(elementName="FullName")
    public void fullName(String value) {
        if(isMissing) {
            LOG.error("Missing PDB file for " + value + "\n did you use a file reference instead of a project reference ?");
            super.setError();
        }
        isMissing=false;
    }
}
