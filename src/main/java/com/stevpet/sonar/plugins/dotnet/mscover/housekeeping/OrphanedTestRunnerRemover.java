package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class OrphanedTestRunnerRemover {

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
                        processHelper.killProcess(engineInfo.getId());
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
