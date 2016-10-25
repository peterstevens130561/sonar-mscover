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
package com.stevpet.sonar.plugins.common.commandexecutor;

import com.stevpet.sonar.plugins.common.api.ShellCommand;


/**
 * Simple cross process locking mechanism, prevents that other sonar-runner processes execute
 * the same commands at the same time. 
 * @author stevpet
 *
 */
public class LockedWindowsCommandLineExecutor extends
        WindowsCommandLineExecutor {
    private final ProcessLock processLock;

    @SuppressWarnings("ucd")
    public LockedWindowsCommandLineExecutor(ProcessLock processLock) {
        this.processLock=processLock;
    }

    @Override
    public int execute(ShellCommand command) {
        String lockName=command.getExecutable();
        processLock.lock(lockName);
        try {
            return super.execute(command);
        } finally {
            processLock.release();
        }
    }
}