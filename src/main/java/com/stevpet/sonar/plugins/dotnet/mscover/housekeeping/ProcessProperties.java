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
package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.Dictionary;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.internal.google.common.base.Preconditions;

/**
 * Properties of a process
 * @author stevpet
 *
 */
public class ProcessProperties {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessProperties.class);
    private Dictionary<String,String> properties = new Hashtable<>(60);
    
    public ProcessProperties(String processBlock) {
        Preconditions.checkArgument(processBlock !=null,"ProcessBlock can't be null");
        String processLines[] = processBlock.split("\r\n");
            for(int line=0;line<processLines.length;line++) {
                String currentLine = processLines[line] ;
                if(currentLine.isEmpty()) {
                    continue;
                }
                Property property = new Property(currentLine);
                properties.put(property.getKey(),property.getValue());       
            }
        }

 
    private String get(String key) {
        return properties.get(key);
    }
    
    public String getCommandLine() {
        String commandLine=get("CommandLine");
        LOG.info("CommandLine {}",commandLine);
        return commandLine;
    }

    public int size() {
        return properties.size();
    }

    public String getParentProcessId() {
        return get("ParentProcessId");
    }
    
    public String getProcessId() {
        return get ("ProcessId");
    }
}
