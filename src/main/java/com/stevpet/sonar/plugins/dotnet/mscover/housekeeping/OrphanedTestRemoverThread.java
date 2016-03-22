package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;

public class OrphanedTestRemoverThread implements Runnable {
    private final static Logger LOG = LoggerFactory.getLogger(OrphanedTestRemoverThread.class);
    private enum State {
        RUNNING,STOPPING,STOPPED
    }
    private State state;
    @Override
    public void run() {
        state=State.RUNNING;
        LOG.info("cleaner starting");
        OrphanedTestRunnerRemover cleaner = new OrphanedTestRunnerRemover(new ProcessHelper(new WindowsCommandLineExecutor()));
        while(state!=State.STOPPING) {
            LOG.info("cleaner busy");
            cleaner.execute();
            try {
                LOG.info("cleaner sleeping");
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                state=State.STOPPED;
                return;
            }
            
        }
        LOG.info("Stopped");
        state=State.STOPPED;
    }

    public void stop() {
        state=State.STOPPING;
        while(state!=State.STOPPED) {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
               Thread.interrupted();
            }
        }
    }

}
