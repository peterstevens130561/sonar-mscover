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
package com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter;

import org.sonar.api.resources.Resource;

public class FileResourceFilter  {
    private String includedPaths;
    private String excludedPaths;

    /**
     * neither included, nor excluded: true
     * not included, excluded set, if it matches excluded then false, otherwise true
     * included set, excloded not set, if it matches included then true, otherwise false
     * both set: if it matches excluded then false. if it matches included, then true, otherwise false
     * 
     * @param resource
     * @return
     */
    public boolean isIncludedFileResource(Resource resource) {
        if(includedPaths ==null && excludedPaths==null) {
            return true;
        }
        return true ;
    }
    
    /**
     * 
     * @param includedPaths
     */
    public void setIncluded(String includedPaths) {
        this.includedPaths = includedPaths;
    }
    
    public void setExcluded(String excludedPaths) {
        this.excludedPaths = excludedPaths;
    }
}
