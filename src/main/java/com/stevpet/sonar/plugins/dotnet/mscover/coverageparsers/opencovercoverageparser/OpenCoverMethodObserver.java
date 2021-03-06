/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;
import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

/**
 * observer is used to find the methods that may be used as unit tests.
 * The code is straightforward, ignoring those methods that do not match the patterns. i.e.
 * anonymous methods
 * @author stevpet
 *
 */
public class OpenCoverMethodObserver implements ParserObserver {

    private MethodToSourceFileIdMap registry;
    protected String moduleName;
    protected String className;
    
    protected String methodName;
    protected String nameSpaceName;


    protected enum ScanMode {
        SCAN,
        SKIP
    }
    
    protected ScanMode scanMode=ScanMode.SCAN ;
    public void setRegistry(MethodToSourceFileIdMap registry) {
        this.registry = registry;
    }
 
    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
        registrar.inPath("Modules/Module")
            .onElement("ModulePath", this::setModuleName);
        
        registrar.inPath("Modules/Module/Classes/Class").onElement("FullName", this::setNamespaceAndClassName);
        registrar.inPath("Modules/Module/Classes/Class/Methods/Method")
            .onElement("Name", this::setMethodName)
            .inElement("FileRef").onAttribute("uid",this::setFileId);
          
    }
    
    /**
     * The moduleName is like
     *       <ModuleName>BHI.JewelEarth.ThinClient.Common</ModuleName>
     * @param value
     */
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
    public void setFileId(String sourceFileId) {
        if(scanMode == ScanMode.SKIP) {
            scanMode=ScanMode.SCAN;
            return;
        }
        MethodId method = new MethodId(moduleName,nameSpaceName,className,methodName);
       
        registry.add(method, sourceFileId);
    }

	public String getModuleName() {
		return moduleName;
	}

	public String getNameSpaceName() {
		return nameSpaceName;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}



}

