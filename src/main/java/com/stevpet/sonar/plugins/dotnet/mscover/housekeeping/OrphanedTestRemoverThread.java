package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;

public class OrphanedTestRemoverThread implements Runnable {
    private final static Logger LOG = LoggerFactory.getLogger(OrphanedTestRemoverThread.class);
    private enum State {
        RUNNING,STOPPING,STOPPED
    }
    private State state = State.RUNNING;
    private OrphanedTestRunnerRemover cleaner;
    @Override
    public void run() {
        int wait=1000;
        Thread.currentThread().setName("OrphanedTestRunnerRemover");
        LOG.info("cleaner starting");
        cleaner = new OrphanedTestRunnerRemover(new ProcessHelper(new WindowsCommandLineExecutor()));
        while(state!=State.STOPPING) {
            cleaner.execute();
            try {
                Thread.sleep(wait);
                wait = wait<60000?wait*2:60000;
            } catch (InterruptedException e) {
                state=State.STOPPED;
                return;
            }
            
        }
        state=State.STOPPED;
    }

    public void stop() {
        LOG.info("please stop");
        state=State.STOPPING;
        while(state!=State.STOPPED) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
               Thread.interrupted();
            }
        }
        LOG.info("removed {} orphans",cleaner.getOrphans().size());
    }

}
