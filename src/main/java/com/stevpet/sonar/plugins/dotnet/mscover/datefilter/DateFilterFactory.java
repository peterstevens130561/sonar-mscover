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
package com.stevpet.sonar.plugins.dotnet.mscover.datefilter;

import org.sonar.api.batch.TimeMachine;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public class DateFilterFactory {
    private DateFilterFactory() {
        
    }
    public static DateFilter createCutOffDateFilter(TimeMachine timeMachine,
            MsCoverProperties helper) {
        String cutOffDate = helper.getCutOffDate();
        DateFilter filter;
        if (cutOffDate == null) {
            filter = new AlwaysPassThroughDateFilter();
        } else {
            filter = new CutOffDateFilter();
        }
        filter.setTimeMachine(timeMachine);
        filter.setCutOffDate(cutOffDate);
        return filter;
    }
    
    public static DateFilter createEmptyDateFilter() {
        return  new AlwaysPassThroughDateFilter();
    }
}
