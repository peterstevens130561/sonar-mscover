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

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.WildcardPattern;

public class AntPatternResourceFilter implements ResourceFilter {
    private WildcardPattern[] exclusionMatchers;
    private WildcardPattern[] inclusionMatchers;
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.FileFilter#setExclusions(java.lang.String)
     */
    public void setExclusions(String pattern) {
        exclusionMatchers=createMatchers(pattern);   
    }
    
    public void setInclusions(String pattern) {
        inclusionMatchers=createMatchers(pattern);      
    }

    private WildcardPattern[] createMatchers(String pattern) {
        if(pattern==null) {
            return null;
        }
        String[] patterns = pattern.split(",");
        return WildcardPattern.create(patterns);         
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.FileFilter#isPassed(java.lang.String)
     */
    public boolean isPassed(String longName) {
        if(StringUtils.isEmpty(longName)) {
            return false;
        }
        String antName = longName.replaceAll("\\\\", "/");
        if(exclusionMatchers==null) {
            return true;
        }
        boolean isExcluded=WildcardPattern.match(exclusionMatchers,antName);
        if(isExcluded) {
            return false ;
        }

        if(inclusionMatchers == null) {
            return true;
        }
        return WildcardPattern.match(inclusionMatchers,antName);
    }
    
    public boolean isIncluded(String longName) {
        String antName = longName.replaceAll("\\\\", "/");
        if(inclusionMatchers == null) {
            return false;
        }
        return WildcardPattern.match(inclusionMatchers,antName);
    }
}
