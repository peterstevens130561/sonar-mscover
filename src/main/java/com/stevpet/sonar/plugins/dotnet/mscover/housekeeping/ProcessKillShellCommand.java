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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.command.Command;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.common.api.ShellCommand;

public class ProcessKillShellCommand implements ShellCommand {

    private final static Logger LOG = LoggerFactory.getLogger(ProcessKillShellCommand.class);
    private String id;
    public ProcessKillShellCommand(String processId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(processId),"id not set");
        this.id=processId;
    }

    @Override
    public String toCommandLine() {
        return toCommand().toString();
    }

    @Override
    public Command toCommand() {
        LOG.info("Terminating process {}",id);
        Command command = Command.create("WMIC");
        command.addArgument("process");
        command.addArgument("where");
        command.addArgument("processid=\"" + id + "\"");
        command.addArgument("call");
        command.addArgument("terminate");
        return command;
    }

    @Override
    public String getExecutable() {
        return "WMIC";
    }

}
