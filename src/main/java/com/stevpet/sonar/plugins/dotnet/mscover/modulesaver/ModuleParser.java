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
package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

public interface ModuleParser {

	/**
	 * parses the coverage data in the stringWriter to get the moduleName
	 * @param module - proper coveragefile that has one module
	 */
	void parse(String module);
	String getModuleName();
	/**
	 * Check whether the module is skipped (i.e. because of filter or no pdb)
	 * @return
	 */
    boolean getSkipped();
    
    /**
     * 
     * @return true if there are 0 lines covered.
     */
    boolean isNotCovered();
}
