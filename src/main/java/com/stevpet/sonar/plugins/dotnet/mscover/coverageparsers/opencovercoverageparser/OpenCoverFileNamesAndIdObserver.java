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

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;
import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;

public class OpenCoverFileNamesAndIdObserver implements ParserObserver {

    
    private SourceFileNameTable registry ;
    private String fileId;
    private String filePath;

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
        fileId=attributeValue;
    }
    
    public void fileMatcher(String attributeValue) {
        filePath=attributeValue;
        registry.add(fileId,filePath);
    }

    public int getUid() {
    	return Integer.parseInt(fileId);
    }
    
    public String getFileName() {
    	return filePath;
    }
}
