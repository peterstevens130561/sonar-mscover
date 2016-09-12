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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;
import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

public class VsTestMethodObserver implements ParserObserver {

    private MethodToSourceFileIdMap methodToSourceFileIdMap;
    private String nameSpaceName;
    private String className;
    private String methodFullName;
    private String moduleName;
    private boolean lookForLine;

    public void setRegistry(MethodToSourceFileIdMap methodToSourceFileIdMap) {
        this.methodToSourceFileIdMap = methodToSourceFileIdMap;

    }
    
    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
        registrar.inPath("Module").onElement("ModuleName",value -> this.moduleName=value)
            .inPath("NamespaceTable").onElement("NamespaceName",value-> this.nameSpaceName=value)
                .inPath("Class").onElement("ClassName", value -> this.className = value)
                    .inPath("Method").onElement("MethodFullName",this::methodFullNameMatcher)
                        .inPath("Lines").onElement("SourceFileID",this::lineIDMatcher);

    }


    public void methodFullNameMatcher(String methodSignature) {
        methodFullName = methodSignature;
        lookForLine = true;
    }

    public void lineIDMatcher(String sourceFileID) {
        if (lookForLine) {
            lookForLine = false;
            MethodId methodId = new MethodId(moduleName, nameSpaceName,
                    className, methodFullName);
            methodToSourceFileIdMap.add(methodId, sourceFileID);
        }

    }

}
