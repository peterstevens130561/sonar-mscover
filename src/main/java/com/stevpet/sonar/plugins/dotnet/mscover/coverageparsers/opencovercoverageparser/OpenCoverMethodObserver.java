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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;
import com.stevpet.sonar.plugins.common.parser.ObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

/**
 * observer is used to find the methods that may be used as unit tests.
 * The code is straightforward, ignoring those methods that do not match the patterns. i.e.
 * anonymous methods
 * @author stevpet
 *
 */
public class OpenCoverMethodObserver extends BaseParserObserver {

    private MethodToSourceFileIdMap registry;
    protected String moduleName;
    protected String className;
    
    protected String methodName;
    protected String nameSpaceName;
    public OpenCoverMethodObserver() {
        setPattern("(Modules/Module/ModuleName)|" +
                "(" + OpenCoverPaths.MODULE_FULLPATH + ")|" +
                "(Modules/Module/Classes/Class/FullName)|" +
                "(Modules/Module/Classes/Class/Methods/Method/Name)|" +
                "(Modules/Module/Classes/Class/Methods/Method/FileRef)|");

    }

    protected enum ScanMode {
        SCAN,
        SKIP
    }
    
    protected ScanMode scanMode=ScanMode.SCAN ;
    public void setRegistry(MethodToSourceFileIdMap registry) {
        this.registry = registry;
    }
 
    /**
     * The moduleName is like
     *       <ModuleName>BHI.JewelEarth.ThinClient.Common</ModuleName>
     * @param value
     */
    @PathMatcher(path=OpenCoverPaths.MODULE_FULLPATH)
    public void setModuleName(String value) {
        scanMode=ScanMode.SCAN;
        String regex = ".*\\\\(.*)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(value) ;
        if(!matcher.find()) {
            scanMode=ScanMode.SKIP;
            return;
        }
        this.moduleName = matcher.group(1);
    }
    
    /**
     * Derive the namespace name from the classname as below
     * <FullName>BHI.JewelEarth.ThinClient.Common.AsyncPassthroughStream</FullName>
     * the last part is the classname, everything before is the namespace
     * @param value
     */
    @PathMatcher(path="Modules/Module/Classes/Class/FullName")
    public void setNamespaceAndClassName(String value) {
        if("<Module>".equals(value)) {
            this.nameSpaceName="";
            this.className="";
            return;
        }
        String regex = "(.*)\\.(.*)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(value) ;
        if(!matcher.find() || matcher.groupCount() !=2) {
            scanMode=ScanMode.SKIP;
            return;
        }
        this.nameSpaceName = matcher.group(1);
        this.className=matcher.group(2);
                

    }
    
    /**
     * methodname i.e
     * <Name>System.Boolean BHI.JewelEarth.ThinClient.Common.AsyncPassthroughStream::get_CanRead()</Name>
     * @param value
     */
    @ElementMatcher(elementName="Name")
    public void setMethodName(String value){
        String regex = "::(.*)\\(.*\\)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(value) ;
        if(!matcher.find() || matcher.groupCount() !=1) {
            scanMode=ScanMode.SKIP;
            return;
        }
        this.methodName = matcher.group(1); 
    }
   


    /**
     * Is last element
     * <FileRef uid="1" />
     * @param value
     */
    @AttributeMatcher(elementName="FileRef",attributeName="uid")
    public void setFileId(String sourceFileId) {
        if(scanMode == ScanMode.SKIP) {
            scanMode=ScanMode.SCAN;
            return;
        }
        MethodId method = new MethodId(moduleName,nameSpaceName,className,methodName);
       
        registry.add(method, sourceFileId);
    }

    @Override
    public void registerObservers(ObserverRegistrar registrar) {
        // TODO Auto-generated method stub
        
    }
    

}

