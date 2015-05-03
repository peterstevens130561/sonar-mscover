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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;

/**
 * Used to parse only those modules that are of interest to this project. Especially for imported coverage files this
 * saves a lot of time
 * @author stevpet
 *
 */
public class ModuleNameObserver extends VsTestCoverageObserver {
    private static final Logger LOG  = LoggerFactory.getLogger(ModuleNameObserver.class);   
    private List<String> modulesToParse = new ArrayList<String>();

    public ModuleNameObserver() {
        setPattern("Module/ModuleName");
    }
    
    /**
     * modules with name (including the .dll, or .exe part) in the list will be parsed, all others will be ignored
     * @param modules modules to parse
     */
    public void addModulesToParse(List<String> modules) {
    	if(modules==null) {
    		return;
    	}
        for(String  module : modules) {
            modulesToParse.add(module.toLowerCase());
        }
    }
   
    @ElementMatcher(elementName="ModuleName")
    public void moduleNameMatcher(String value) {
        if(modulesToParse.isEmpty()) {
            return;
        }
        boolean shouldSkip=!modulesToParse.contains(value);
        if(!shouldSkip) {
            LOG.info("Module {} will be parsed",value);           
        }
        if(shouldSkip) {
            setSkipTillNextElement("Module");
        }
    }

	public void setVsTestRegistry(SonarCoverage registry) {
		// Ignoring intentionally
	}
}
