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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;


import com.stevpet.sonar.plugins.dotnet.mscover.model.LineModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.LinesRegistry;


public class VsTestLinesToLinesObserver extends BaseParserObserver{

    private LinesRegistry registry ;
    private LineModel model = new LineModel();

    public VsTestLinesToLinesObserver() {
        setPattern("Module/NamespaceTable/Class/Method/Lines/(LnStart|Coverage|SourceFileID)");
    }


    @ElementMatcher(elementName="LnStart")
    public void lnStartMatcher(String value) {
        model = new LineModel();
        model.setLnStart(value);
    }
    
    @ElementMatcher(elementName="Coverage")
    public void coverageMatcher(String value) {
        model.setCoverage(value);
    }
    
    @ElementMatcher(elementName="SourceFileID")
    public void sourceFileIdMatcher(String fileId){
         registry.add(fileId, model);  
    }
    
    public void setRegistry(LinesRegistry registry) {
        this.registry=registry;
    }

}
