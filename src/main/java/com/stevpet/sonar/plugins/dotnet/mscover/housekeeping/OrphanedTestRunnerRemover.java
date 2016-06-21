package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrphanedTestRunnerRemover {
    private final static Logger LOG = LoggerFactory.getLogger(OrphanedTestRunnerRemover.class);
    private ProcessHelper processHelper;
    private List<ProcessInfo> engines;
    private List<ProcessInfo> consoles;
    private List<ProcessInfo> opencovers;

    public OrphanedTestRunnerRemover(ProcessHelper processLister) {
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
                        processHelper.killProcess(engineId);
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
