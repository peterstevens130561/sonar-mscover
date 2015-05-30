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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;


public class MethodToSourceFileIdMap {
    
    private static final Logger LOG = LoggerFactory
            .getLogger(MethodToSourceFileIdMap.class);
    private Map<MethodId,String> methodRegistry = new HashMap<MethodId,String>();

   
    public void add(MethodId methodId,String sourceFileId) {
        MethodId methodClone = methodId.deepClone();
        methodRegistry.put(methodClone, sourceFileId);
    }
    
    public String get(MethodId methodId) {
        if(methodId==null) {
            return null;
        }
        String fileId=methodRegistry.get(methodId);
        return fileId;
    }
 
    public String getLongestContainedMethod(MethodId methodId) {
        if(methodId==null) {
            return null;
        }
        String fileId;
        do {
            fileId=methodRegistry.get(methodId);
            if(fileId!= null) {
                break;
            }
            String name=methodId.getMethodName();
            String shorterName;
            if(name.matches("_.*")) {
                shorterName=name.substring(1);
            } else {
                shorterName=name.substring(0, name.length()-1);
            }
            methodId.setMethodName(shorterName);
        } while (!methodId.getMethodName().isEmpty());
        return fileId;
    }
    public int size() {
        return methodRegistry.size();
    }

}
