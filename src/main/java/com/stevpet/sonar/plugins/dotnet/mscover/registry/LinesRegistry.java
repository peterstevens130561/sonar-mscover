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
package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.util.HashMap;
import java.util.Map;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLines;
import com.stevpet.sonar.plugins.dotnet.mscover.model.LineModel;

public class LinesRegistry  {
    Map<String,FileLines> fileLines = new HashMap<String,FileLines>();
    
    public void add(String fileId, LineModel line) {
        if(!fileLines.containsKey(fileId)) {
            fileLines.put(fileId,new FileLines());
        }
        fileLines.get(fileId).add(line);
    }


    public FileLines get(String fileId) {
        return fileLines.get(fileId);
    }

    public int size() {
        return fileLines.size();
    }

}
