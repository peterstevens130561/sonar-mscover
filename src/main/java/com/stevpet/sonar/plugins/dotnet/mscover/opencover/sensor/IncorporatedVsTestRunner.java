package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;

public class IncorporatedVsTestRunner {
    
    private final Logger LOG = LoggerFactory.getLogger(IncorporatedVsTestRunner.class);
    private final MsCoverProperties propertiesHelper;
    private final MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private final FileSystem fileSystem;

    IncorporatedVsTestRunner (MsCoverProperties propertiesHelper, 
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            FileSystem fileSystem) {
        this.propertiesHelper = propertiesHelper;
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment;
        this.fileSystem = fileSystem;
    }
    
    VsTestRunner build() {
        VsTestRunner unitTestRunner = WindowsVsTestRunner.create();
        unitTestRunner.setPropertiesHelper(propertiesHelper);
        VisualStudioSolution solution=microsoftWindowsEnvironment.getCurrentSolution();
        if(solution == null) {
            String msg = "No current solution";
            LOG.error(msg);
            throw new SonarException(msg);
        }
        unitTestRunner.setSolution(solution);
        
        String sonarWorkingDirectory=fileSystem.workDir().getAbsolutePath();
        String coverageXmlPath =sonarWorkingDirectory + "/coverage.xml";
        unitTestRunner.setCoverageXmlPath(coverageXmlPath);
        unitTestRunner.setSonarPath(sonarWorkingDirectory);
        return unitTestRunner;
    }
}
