package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.common.commandexecutor.TimeoutException;
import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;

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
        OrphanedTestRunnerRemover cleaner = new OrphanedTestRunnerRemover(new ProcessHelper(new WindowsCommandLineExecutor()));
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
                throw new SonarException("Thread did not close in time");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
               Thread.interrupted();
            }
        }
    }

}
