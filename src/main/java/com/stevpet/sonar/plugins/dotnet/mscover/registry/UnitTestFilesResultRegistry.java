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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;

public class UnitTestFilesResultRegistry {
    Logger LOG = LoggerFactory.getLogger(UnitTestFilesResultRegistry.class);
    //Key is FileId
    Map<String,ClassUnitTestResult> unitTestFilesResultRegistry = new HashMap<String,ClassUnitTestResult>();

    /**
     * Map unit tests to sourcefiles
     * @param unitTestingResults - holds unit tests
     * @param map - holds map from method to source file id 
     */
    public void mapMethodsToFileId(UnitTestingResults unitTestingResults, MethodToSourceFileIdMap map) {
        Collection<UnitTestMethodResult>unitTests=unitTestingResults.values();
        for(UnitTestMethodResult unitTest:unitTests) {
            MethodId methodId=unitTest.getMethodId();
            String fileId = map.getLongestContainedMethod(methodId);
            bailOutOnNotFound(map, methodId, fileId);
            if(!unitTestFilesResultRegistry.containsKey(fileId)) {
                unitTestFilesResultRegistry.put(fileId, new ClassUnitTestResult());
            }
            ClassUnitTestResult unitTestFileResults = unitTestFilesResultRegistry.get(fileId);
            unitTestFileResults.add(unitTest);
        }
    }

    private void bailOutOnNotFound(MethodToSourceFileIdMap map,
            MethodId methodId, String fileId) {
        if(fileId==null) {
            map.dumpMap();
            String msg = createPrettyMessage(methodId);
            LOG.error(msg);
            throw new MsCoverCanNotFindSourceFileForMethodException(msg);
        }
    }

    private String createPrettyMessage(MethodId methodId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Can't find sourcefile for the following method\n");
        sb.append("One known cause is that the method is inherited");
        sb.append("\nassembly  :").append(methodId.getModuleName());
        sb.append("\nnamespace :").append(methodId.getNamespaceName());
        sb.append("\nclass     :").append(methodId.getClassName());
        sb.append("\nmethod    :").append(methodId.getMethodName());
        sb.append("\nhash      :").append(methodId.hashCode());
        return sb.toString();
    }

    public interface ForEachUnitTestFile {
        void execute(String fileId,ClassUnitTestResult unitTest);
    }
    
    public void forEachUnitTestFile(ForEachUnitTestFile action) {
        for(Entry<String, ClassUnitTestResult>entry: unitTestFilesResultRegistry.entrySet()) {
            String fileId = entry.getKey();
            ClassUnitTestResult unitTest=entry.getValue();
            action.execute(fileId, unitTest);
        }
        
    }

	public void mapMethodsToFileId(UnitTestingResults testingResults,
			MethodToSourceFileIdMap map,
			SourceFileNameTable sourceFileNamesTable) {
		// TODO Auto-generated method stub
		
	}
}
