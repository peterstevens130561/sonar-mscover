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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrphanedTestRunnerRemover {
    private final static Logger LOG = LoggerFactory.getLogger(OrphanedTestRunnerRemover.class);
    private MwicBridge processHelper;
    private List<ProcessInfo> engines;
    private List<ProcessInfo> consoles;
    private List<ProcessInfo> opencovers;

    public OrphanedTestRunnerRemover(MwicBridge processLister) {
        this.processHelper=processLister;
    }

    public void execute() {
        engines=processHelper.getProcessInfoFromName("vstest.executionengine.exe");
        consoles=processHelper.getProcessInfoFromName("vstest.console.exe");
        opencovers=processHelper.getProcessInfoFromName("opencover.console.exe");
        for(ProcessInfo consoleInfo : consoles) {
            if(isOrphaned(consoleInfo)){
                for(ProcessInfo engineInfo : engines) {
                    if(engineInfo.parentId.equals(consoleInfo.getId())) {
                        String engineId=engineInfo.getId();
                        LOG.info("cleaner will kill {}",engineId);
                        processHelper.killProcessId(engineId);
                    }
                }
            }
        }
    }

    private boolean isOrphaned(ProcessInfo consoleInfo) {
        String parentId = consoleInfo.getParentId();
        for(ProcessInfo opencoverInfo : opencovers) {
            if(opencoverInfo.getId().equals(parentId)) {
                return false;
            }
        }
        return true;
    }


}
