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
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.parser.observer.TopLevelObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;

public class OpenCoverFileNamesAndIdObserver extends BaseParserObserver {

    
    private SourceFileNameTable registry ;
    private SourceFileNameRow model;

    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
    	registrar.inPath("Modules/Module/Files").inElement("File")
    		.onAttribute("uid", this::uidMatcher)
    		.onAttribute("fullPath", this::fileMatcher);
    	
    }
    
    public void setRegistry(SourceFileNameTable registry) {
        this.registry=registry;
    }

    public void uidMatcher(String attributeValue) {
        model = new SourceFileNameRow();
        model.setSourceFileID(Integer.parseInt(attributeValue));
    }
    
    public void fileMatcher(String attributeValue) {
        model.setSourceFileName(attributeValue);
        registry.add(model);
    }

    public int getUid() {
    	return model.getSourceFileID();
    }
    
    public String getFileName() {
    	return model.getSourceFileName();
    }
}
