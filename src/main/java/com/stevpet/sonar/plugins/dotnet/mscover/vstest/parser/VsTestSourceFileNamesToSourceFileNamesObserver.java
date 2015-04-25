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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestCoverageObserver;


public class VsTestSourceFileNamesToSourceFileNamesObserver extends VsTestCoverageObserver{

    private SourceFileNameTable registry ;
    private SourceFileNameRow model;
    public VsTestSourceFileNamesToSourceFileNamesObserver() {
        setPattern("SourceFileNames/(SourceFileID|SourceFileName)");
    }


    @ElementMatcher(elementName="SourceFileID")
    public void sourceFileIDMatcher(String value) {
    	model = registry.getNewRow(value);
    }

    @ElementMatcher(elementName="SourceFileName")
    public void sourceFileNameMatcher(String value){
    	model.setSourceFileName(value);
    }

    public void setRegistry(SourceFileNameTable registry) {
        this.registry=registry;
    }


    @Override
    public void setVsTestRegistry(VsTestCoverageRegistry vsTestRegistry) {
        this.registry=vsTestRegistry.getSourceFileNameTable();
    }


}
