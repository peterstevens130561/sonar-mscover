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
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;

/**
 * @author stevpet
 *
 */
public class MethodToSourceFileIdMap {
    
    private Map<MethodId,String> map = new HashMap<MethodId,String>();
   
    public void add(MethodId methodId,String sourceFileId) {
        map.put(methodId, sourceFileId);
        MethodId fallBackMethodId=methodId.getFallBack();
        map.put(fallBackMethodId, sourceFileId);
    }
    
    /**
     * First it will try to find an exact match, if that fails, it will try to find the same method in the same namespace and module, ignoring the class
     * This is sometimes needed to find test methods that are inherited
     * 
     * @param methodId
     * @return
     */
    public String get(MethodId methodId) {
        if(methodId==null) {
            return null;
        }
        String fileId=map.get(methodId);
        if(fileId==null) {
        	MethodId fallBackMethodId=methodId.getFallBack();
        	fileId=map.get(fallBackMethodId);
        }
        return fileId;
    }
 
    /**
     * the number of methods added
     * @return
     */
    public int size() {
        return map.size()/2;
    }

}
