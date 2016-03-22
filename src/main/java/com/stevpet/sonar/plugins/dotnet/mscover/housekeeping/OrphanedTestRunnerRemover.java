package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

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
        if(opencovers.size()==0 && engines.size()>0){
            for(ProcessInfo processInfo : engines) {
                processHelper.killProcess(processInfo.getId());
            }
        }
    }


}
