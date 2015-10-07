/*
 * Sonar .NET Plugin :: ReSharper
 * Copyright (C) 2013 John M. Wright
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
package com.stevpet.sonar.plugins.dotnet.resharper;

import org.sonar.api.rules.RulePriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReSharperUtils {
	
    private static final Logger LOG = LoggerFactory.getLogger(ReSharperUtils.class);
	  //http://www.jetbrains.com/resharper/webhelp/Reference__Options__Code_Inspection__Inspection_Severity.html
    //http://www.jetbrains.com/resharper/webhelp/Code_Analysis__Code_Highlighting.html

    public static ReSharperSeverity getResharperSeverity(String severity) {
        ReSharperSeverity result;
    	try {
            result=ReSharperSeverity.valueOf(severity);
        } catch (Exception ex)
        {
            result=ReSharperSeverity.WARNING;
            LOG.warn("Invalid severity"  + severity);
        }
    	return result;
    }

    /**
     * translate the resharper severity into sonar priority
     */
    public static  RulePriority translateResharperPriorityIntoSonarPriority(ReSharperSeverity severity) {
        switch (severity) {
        case ERROR:
            return RulePriority.CRITICAL;
        case WARNING:
            return RulePriority.MAJOR;
        case SUGGESTION:
            return RulePriority.MINOR;
        case HINT:
        case INFO:
        case DO_NOT_SHOW:
            return RulePriority.INFO ;
        default:
            return null;
    }
    }

    /**
     * translate sonar priority into resharper severity
     * @param priority
     * @return
     */
   public static  ReSharperSeverity translateSonarPriorityIntoResharperSeverity(RulePriority priority) {

       switch (priority) {
       case BLOCKER:
           return ReSharperSeverity.ERROR;
       case CRITICAL:
           return ReSharperSeverity.ERROR;
       case MAJOR:
           return ReSharperSeverity.WARNING;
       case MINOR:
           return ReSharperSeverity.SUGGESTION;
       case INFO:
           return ReSharperSeverity.HINT;
       default:
           return null;
   }

    }

}
