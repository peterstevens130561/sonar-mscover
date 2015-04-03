package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;

public class InjectingFakesRemover extends DefaultFakesRemover {
    private AssembliesFinder assembliesFinder;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

    public InjectingFakesRemover(AssembliesFinder assembliesFinder,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.assembliesFinder = assembliesFinder;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }
    
    public void execute() {
        VisualStudioSolution solution = microsoftWindowsEnvironment.getCurrentSolution();
        String targetDir=assembliesFinder.findUnitTestAssembliesDir(solution);
        super.removeFakes(new File(targetDir));
    }
}