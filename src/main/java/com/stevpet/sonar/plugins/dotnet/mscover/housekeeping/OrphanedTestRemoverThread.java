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
package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;

/**
 * Use to remove dangling test runners
 * 
 * Start at the beginning of a test run, at the end stop it.
 *
 */
public class OrphanedTestRemoverThread implements Runnable {
    private final static Logger LOG = LoggerFactory.getLogger(OrphanedTestRemoverThread.class);
    private enum State {
        RUNNING,STOPPING,STOPPED
    }
    private State state = State.RUNNING;
    @Override
    public void run() {
        int wait=1000;
        Thread.currentThread().setName("OrphanedTestRunnerRemover");
        LOG.debug("cleaner starting");
        OrphanedTestRunnerRemover cleaner = new OrphanedTestRunnerRemover(new MwicBridge(new WindowsCommandLineExecutor()));
        while(state!=State.STOPPING) {
            LOG.debug("State {} ",state);
            LOG.debug("cleaner busy");
            cleaner.execute();
            try {
                LOG.debug("cleaner sleeping");
                Thread.sleep(wait);
                wait = wait<60000?wait*2:60000;
            } catch (InterruptedException e) {
                state=State.STOPPED;
                return;
            }
            
        }
        LOG.debug("Stopped");
        state=State.STOPPED;
    }

    public void stop() {
        LOG.debug("please stop");
        state=State.STOPPING;
        int ctr=0;
        while(state!=State.STOPPED) {
            ++ctr;
            if(ctr>100) {
                throw new IllegalStateException("Thread did not close in time");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
               Thread.interrupted();
            }
        }
    }

}
